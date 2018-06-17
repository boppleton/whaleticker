package gui.market;

import gui.market.MarketOrder;
import gui.market.MarketOrderCell;
import gui.market.MarketOrdersTableModel;
import websocket.Broadcaster;
import websocket.TradeUni;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.util.ArrayList;

public class MarketWindow extends JFrame implements Broadcaster.BroadcastListener {

    private ArrayList orders;

    private JScrollPane tradesScrollPane;
    private JTable tradesTable;
    private JTableHeader tableHeader;

    private boolean alwaysOnTop = false;
    private boolean hideFrame = false;
    private boolean showScrollbar = false;

    public MarketWindow(String title) {
        super(title);

        Broadcaster.register(this);

        orders = new ArrayList();
        orders.add(new MarketOrder("bitmex", 9000, 3));
        orders.add(new MarketOrder("bitmex", 80000, 3));
        orders.add(new MarketOrder("bitmex", 300000, 3));
        orders.add(new MarketOrder("bitfinex", 700000, 3));
        orders.add(new MarketOrder("bitfinex", 1100000, 3));
        orders.add(new MarketOrder("bitfinex", 90000, 3));
        orders.add(new MarketOrder("bitfinex", 500, 3));
        orders.add(new MarketOrder("bitmex", 200, 3));
        orders.add(new MarketOrder("bitmex", 300, 3));
        orders.add(new MarketOrder("bitmex", 100, 3));
        orders.add(new MarketOrder("bitmex", -500, 3));
        orders.add(new MarketOrder("bitmex", -500, 3));
        orders.add(new MarketOrder("bitmex", -800, 3));
        orders.add(new MarketOrder("bitmex", -900, 3));
        orders.add(new MarketOrder("bitmex", -980, 3));
        orders.add(new MarketOrder("bitmex", 2000, 3));
        orders.add(new MarketOrder("bitmex", 5000, 3));
        orders.add(new MarketOrder("bitmex", 5000, 3));
        orders.add(new MarketOrder("bitmex", 7000, 3));
        orders.add(new MarketOrder("bitmex", 9000, 3));
        orders.add(new MarketOrder("bitmex", -500, 3));
        orders.add(new MarketOrder("bitmex", -2000, 3));
        orders.add(new MarketOrder("bitmex", -5000, 3));
        orders.add(new MarketOrder("bitmex", -5000, 3));
        orders.add(new MarketOrder("bitmex", -7000, 3));
        orders.add(new MarketOrder("bitmex", -9000, 3));
        orders.add(new MarketOrder("bitmex", 25000, 3));
        orders.add(new MarketOrder("bitmex", 100000, 3));
        orders.add(new MarketOrder("bitmex", 100000, 3));
        orders.add(new MarketOrder("bitmex", -100000, 3));
        orders.add(new MarketOrder("bitmex", -100000, 3));
        orders.add(new MarketOrder("bitmex", 1400000, 3));
        orders.add(new MarketOrder("bitmex", 2100000, 3));
        orders.add(new MarketOrder("bitmex", 4100000, 3));
        orders.add(new MarketOrder("bitmex", -1400000, 3));
        orders.add(new MarketOrder("bitmex", -1100000, 3));
        orders.add(new MarketOrder("bitmex", 600000, 3));
        orders.add(new MarketOrder("bitmex", 700000, 3));
        orders.add(new MarketOrder("bitmex", 900000, 3));


        tradesTable = new JTable(new MarketOrdersTableModel(orders));
        tradesTable.setDefaultRenderer(MarketOrder.class, new MarketOrderCell());
        tradesTable.setRowHeight(22);
        tradesTable.setTableHeader(null);
        tradesTable.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    settingsDialog();
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

        tradesScrollPane = new JScrollPane(tradesTable);
        tradesScrollPane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(tradesScrollPane.getVerticalScrollBar().getValue() == 0) {
                    tradesScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
                } else {
                    tradesScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
                }
            }
        });
        tradesScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        tradesScrollPane.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    settingsDialog();
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

        this.add(tradesScrollPane);

    }

    private void settingsDialog() {

        JPanel settingsPanel = new JPanel();

        settingsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        JRadioButton hideFrameRadio = new JRadioButton("hide frame");
        hideFrameRadio.setSelected(hideFrame);
        settingsPanel.add(hideFrameRadio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JRadioButton alwaysOnTopRadio = new JRadioButton("always on top");
        alwaysOnTopRadio.setSelected(alwaysOnTop);
        settingsPanel.add(alwaysOnTopRadio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JRadioButton showScrollbarRadio = new JRadioButton("show scrollbar");
        showScrollbarRadio.setSelected(showScrollbar);
        settingsPanel.add(showScrollbarRadio, gbc);


        int result = JOptionPane.showConfirmDialog(null, settingsPanel, "config", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {

            //ok set settings

            setAlwaysTop(alwaysOnTopRadio.isSelected());

            setShowFrame(hideFrameRadio.isSelected());

            setShowScrollbar(showScrollbarRadio.isSelected());

        }
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

            tradesScrollPane.revalidate();

        });
    }

    private void setShowScrollbar(boolean radio) {

        EventQueue.invokeLater(() -> {

            if (radio && !showScrollbar) {
                tradesScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
                showScrollbar = true;
            } else if (!radio && showScrollbar) {
                tradesScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
                showScrollbar = false;
            }

            tradesScrollPane.revalidate();

        });

    }

    @Override
    public void receiveBroadcast(String message) throws InterruptedException, IOException {



        if (message.contains("bitmex")) {

            boolean side = (message.substring(message.indexOf("!"), message.indexOf("!#")).contains("Buy"));
            final int size = Integer.parseInt(message.substring(message.indexOf("#") + 1, message.indexOf("#@")));

            System.out.println("new bitmex trade " + size);

            EventQueue.invokeLater(() -> {


                orders.add(0, new MarketOrder("bitmex", size, 5));

                //maybe remove some of these
                revalidate();
                tradesTable.revalidate();
                tradesScrollPane.revalidate();
            });

        } else if (message.contains("bitmexliq")) {

//            Toolkit.getDefaultToolkit().beep();

            boolean side = (message.substring(message.indexOf("!"), message.indexOf("!#")).contains("Buy"));
            double amount = Double.parseDouble(message.substring(message.indexOf("#") + 1, message.indexOf("#@")));
            System.out.println("liq amount: " + amount);

            double price = Double.parseDouble(message.substring(message.indexOf("@") + 1, message.indexOf("@*")));
            String action = String.valueOf(message.substring(message.indexOf("*") + 1, message.indexOf("*^")));

            String id = String.valueOf(message.substring(message.indexOf("^") + 1, message.indexOf("^_")));

//            if (action.contains("insert")) {
//                addLiq(new TradeUni("bitmex", Formatter.kFormat(amount, 0) + "", amount, side, price, "time", id));
//            } else if (action.contains("update")) {
//                updateLiq(new TradeUni("bitmex", "", amount, side, price, "time", id));
        } else if (message.contains("bitfinex") || message.contains("bitmex") || message.contains("okex") || message.contains("binance") || message.contains("gdax")) {
//            addTradeData(message, message.substring(0, 1).equals("u"));


        }
    }
}
