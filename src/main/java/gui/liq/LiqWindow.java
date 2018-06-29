package gui.liq;

import utils.Broadcaster;
import utils.Formatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

//todo: liq active/finished icon, price with < change
//click to clear orderbook to see whats happening

public class LiqWindow extends JFrame implements Broadcaster.BroadcastListener {

    private ArrayList<LiqOrder> liqs;

    private JScrollPane liqsScrollPane;
    private JTable liqsTable;

    private boolean alwaysOnTop = false;
    private boolean hideFrame = false;

    private int minLiq = 1;

    public LiqWindow(String title) {
        super(title);

        Image icon = Toolkit.getDefaultToolkit().getImage("/art.png");
        this.setIconImage(icon);

        Broadcaster.register(this);

        liqs = new ArrayList<>();

        setupTableScrollpane();
    }

    @Override
    public void receiveBroadcast(String message) {

        if (message.contains("liq")) {

            boolean side = (message.substring(message.indexOf("!"), message.indexOf("!#")).contains("Buy"));
            int amount = (int) Double.parseDouble(message.substring(message.indexOf("#") + 1, message.indexOf("#@")));
//            System.out.println("liq amount: " + amount);

            double price = Double.parseDouble(message.substring(message.indexOf("@") + 1, message.indexOf("@*")));
            String action = String.valueOf(message.substring(message.indexOf("*") + 1, message.indexOf("*^")));

            String id = String.valueOf(message.substring(message.indexOf("^") + 1, message.indexOf("^_")));


            if (action.contains("insert")) {
                addLiq(new LiqOrder("bitmex", Formatter.kFormat(amount, 0) + "", amount, side, price, "time", id, true));
            } else if (action.contains("update")) {
                updateLiq(new LiqOrder("bitmex", "", amount, side, price, "time", id, true));
            } else if (action.contains("delete")) {
                updateLiq(new LiqOrder("bitmex", "", amount, side, price, "time", id, false));
            }


//            System.out.println("bitmexliq " + amount + " " + (side ? "buy" : "sell") + " " + "action: " + action);
        }
    }

    public void addLiq(LiqOrder t) {

        if (t.getSize() >= minLiq) {

            liqs.add(0, t);

            if (liqs.size() > 100) {
                liqs.remove(liqs.size() - 1);
                liqs.remove(liqs.size() - 1);
                liqs.remove(liqs.size() - 1);
            }

            //maybe remove some of these
            revalidate();
            liqsTable.revalidate();
            liqsScrollPane.revalidate();
        }
    }


    private void updateLiq(LiqOrder tradeUni) {


        for (int i = 0; i < liqs.size(); i++) {

//            System.out.println("looping liqs.  id: " + liqs.get(i).getId() + "tradeuni id: " + tradeUni.getId());

            if (liqs.get(i).getId().equals(tradeUni.getId())) {

//                System.out.println("liq has same id..");
//                        int updaterSpot = liqs.indexOf(liqs.get(i));
//                        String actions = liqs.get(i).getInstrument();

                if (tradeUni.getSize() > 0) {

//                    System.out.println("this update size > 0 ... setting instrument");

                    liqs.get(i).setInstrument(Formatter.kFormat(tradeUni.getSize(), 0) + " < " + liqs.get(i).getInstrument());

                    if (tradeUni.getSize() > liqs.get(i).getSize()) {
                        liqs.get(i).setSize(tradeUni.getSize());
//                        System.out.println("updating size from " + liqs.get(i).getSize() + " to " + tradeUni.getSize());
                    }

//                    System.out.println("ins: " + liqs.get(i).getInstrument());
                }
                if (!tradeUni.isActive()) {
                    liqs.get(i).setActive(false);
                }


            }
        }

        EventQueue.invokeLater(() -> {

            liqsTable.revalidate();
            liqsScrollPane.revalidate();
            revalidate();
            repaint();
        });


    }



    private void settingsDialog() {

        JPanel settingsPanel = new JPanel();

        settingsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();


        gbc.anchor = GridBagConstraints.WEST;

        //size panel
        JPanel liqSizePanel = new JPanel(new GridBagLayout());
        liqSizePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.white, 0)));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel minLabel = new JLabel("minimum liq ");
        liqSizePanel.add(minLabel, gbc);
        gbc.gridx = 1;
        JTextField minimumAmount = new JTextField(Formatter.amountFormat(minLiq), 5);
        liqSizePanel.add(minimumAmount, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        settingsPanel.add(liqSizePanel, gbc);

        //settings panel
        JPanel setPanel = new JPanel(new GridBagLayout());
        setPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.white, 0), "settings"));
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JRadioButton alwaysOnTopRadio = new JRadioButton("always on top");
        alwaysOnTopRadio.setSelected(alwaysOnTop);
        setPanel.add(alwaysOnTopRadio, gbc);
        gbc.gridy = 1;
        JRadioButton hideFrameRadio = new JRadioButton("hide frame");
        hideFrameRadio.setSelected(hideFrame);
        setPanel.add(hideFrameRadio, gbc);
        gbc.gridy = 1;
        settingsPanel.add(setPanel, gbc);

        //send it
        int result = JOptionPane.showConfirmDialog(null, settingsPanel, "settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {

            setAlwaysTop(alwaysOnTopRadio.isSelected());
            setShowFrame(hideFrameRadio.isSelected());
            setMinimumAmt(minimumAmount.getText());
        }
    }

    private void setupTableScrollpane() {

        liqsTable = new JTable(new LiqOrdersTableModel(liqs));
        liqsTable.setDefaultRenderer(LiqOrder.class, new LiqOrderCell());
        liqsTable.setRowHeight(22);
        liqsTable.setTableHeader(null);
        liqsTable.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON3) {
                    settingsDialog();
                } else if (e.getButton() == MouseEvent.BUTTON2) {

                    EventQueue.invokeLater(() -> {
                        liqsTable.removeAll();
                        liqsTable.revalidate();
                    });
                }
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });

        liqsScrollPane = new JScrollPane(liqsTable);
        liqsScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        liqsScrollPane.addMouseWheelListener(e -> {
            if (liqsScrollPane.getVerticalScrollBar().getValue() == 0) {
                liqsScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
            } else {
                liqsScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
            }
        });

        liqsScrollPane.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    settingsDialog();
                }
//                else if (e.getButton() == MouseEvent.BUTTON1) {
//                    Broadcaster.broadcast("(bitmexliq)!" + true + "!#" + 420  + "#@" + 9001 + "@*" + "insert" + "*^" + "ididid" + "^_");
//
//                    liqsTable.revalidate();
//                    liqsScrollPane.revalidate();
//                    revalidate();
//                } else if (e.getButton() == MouseEvent.BUTTON2) {
//                    Broadcaster.broadcast("(bitmexliq)!" + true + "!#" + -1 + "#@" + -1 + "@*" + "delete" + "*^" + "ididid" + "^_");
//
//                    liqsTable.revalidate();
//                    liqsScrollPane.revalidate();
//                    revalidate();
//                }
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });


        this.add(liqsScrollPane);
    }



    private void setShowFrame(boolean radio) {

        EventQueue.invokeLater(() -> {
            if (radio && !hideFrame) {
                dispose();
                setUndecorated(true);
                setVisible(true);

                hideFrame = true;
            } else if (!radio && hideFrame) {
                dispose();
                setUndecorated(false);
                setVisible(true);
                hideFrame = false;
            }
        });
    }

    private void setAlwaysTop(boolean radio) {

        EventQueue.invokeLater(() -> {

            if (radio && !alwaysOnTop) {
                setAlwaysOnTop(true);
                alwaysOnTop = true;
            } else if (!radio && alwaysOnTop) {
                setAlwaysOnTop(false);
                alwaysOnTop = false;
            }
            liqsScrollPane.revalidate();
        });
    }

    private void setMinimumAmt(String minimumAmt) {
        try {
            String minString = minimumAmt.replaceAll("\\D", "");
            this.minLiq = Integer.parseInt(minString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
