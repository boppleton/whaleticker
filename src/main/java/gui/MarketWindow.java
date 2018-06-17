package gui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class MarketWindow  extends JFrame {


    private JScrollPane tradesScrollPane;
    private JTable tradesTable;
    private JTableHeader tableHeader;

    private boolean alwaysOnTop = true;
    private boolean hideFrame = false;
    private boolean showScrollbar = false;

    public MarketWindow(String title) {
        super(title);

        ArrayList orders = new ArrayList();
        orders.add(new MarketOrder("bitmex", 9000, 3));
        orders.add(new MarketOrder("bitmex", 80000, 3));
        orders.add(new MarketOrder("bitmex", 300000, 3));
        orders.add(new MarketOrder("bitmex", 700000, 3));
        orders.add(new MarketOrder("bitmex", 1100000, 3));
        orders.add(new MarketOrder("bitmex", 90000, 3));
        orders.add(new MarketOrder("bitmex", 500, 3));
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

        setAlwaysOnTop(true);


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


        //for hide window frame button
        if (radio && !hideFrame) {
            dispose();
            setUndecorated(true);
            setVisible(true);
//            Point framePosition = this.getLocationOnScreen();
//            Dimension frameSize = this.getSize();
//            this.invalidate();
//            this.dispose();
//            try {
//                window(false, framePosition, frameSize);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            hideFrame = true;
        } else if (!radio && hideFrame) {
            dispose();
            setUndecorated(false);
            setVisible(true);
//            Point framePosition = this.getLocationOnScreen();
//            Dimension frameSize = this.getSize();
//            this.invalidate();
//            this.dispose();
//            try {
//                window(true, framePosition, frameSize);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
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
}
