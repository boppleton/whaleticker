package gui.market;

import gui.market.MarketOrder;
import gui.market.MarketOrderCell;
import gui.market.MarketOrdersTableModel;
import websocket.Broadcaster;
import websocket.Formatter;
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

    private int minimumTradeAmt = 100;
    private int maxTradeAmt = 999999999;

    private ArrayList<String> instruments = new ArrayList<>();
    private JRadioButton bitmexPerpSwapRadio;
    private JRadioButton bitfinexSpotRadio;
    private JRadioButton okexSpotRadio;
    private JRadioButton gdaxSpotRadio;
    private JRadioButton binanceSpotRadio;
    private JRadioButton bitmexJuneRadio;
    private JRadioButton bitmexSeptRadio;
    private JRadioButton okexThisWeekRadio;
    private JRadioButton okexNextWeekRadio;
    private JRadioButton okexQuarterlyRadio;

    public MarketWindow(String title) {
        super(title);

        Broadcaster.register(this);

        orders = new ArrayList(10);
        orders.add(new MarketOrder("bitmex", 9000, 3));
        orders.add(new MarketOrder("bitmex", 80000, 3));


        setupTableScrollpane();

    }

    private void setupTableScrollpane() {

        tradesTable = new JTable(new MarketOrdersTableModel(orders));
        tradesTable.setDefaultRenderer(MarketOrder.class, new MarketOrderCell());
        tradesTable.setRowHeight(22);
        tradesTable.setTableHeader(null);
        tradesTable.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON3) {
                    settingsDialog();
                } else if (e.getButton() == MouseEvent.BUTTON2) {

                    EventQueue.invokeLater(() -> {
                        orders.clear();
//                        revalidate();
                        tradesTable.revalidate();
//                        tradesScrollPane.revalidate();
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

        tradesScrollPane = new JScrollPane(tradesTable);
        tradesScrollPane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (tradesScrollPane.getVerticalScrollBar().getValue() == 0) {
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

        JPanel spotPanel = new JPanel(new GridBagLayout());
        spotPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.white, 0), "spot"));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        bitmexPerpSwapRadio = new JRadioButton("bitmex perp swap", instruments.contains("bitmexPerp"));
        spotPanel.add(bitmexPerpSwapRadio, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        bitfinexSpotRadio = new JRadioButton("bitfinex spot", instruments.contains("bitfinexSpot"));
        spotPanel.add(bitfinexSpotRadio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        okexSpotRadio = new JRadioButton("okex spot", instruments.contains("okexSpot"));
        spotPanel.add(okexSpotRadio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gdaxSpotRadio = new JRadioButton("gdax spot", instruments.contains("gdaxSpot"));
        spotPanel.add(gdaxSpotRadio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        binanceSpotRadio = new JRadioButton("binance spot", instruments.contains("binanceSpot"));
        spotPanel.add(binanceSpotRadio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        settingsPanel.add(spotPanel, gbc);


        JPanel futuresPanel = new JPanel(new GridBagLayout());
        futuresPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.white, 0), "futures"));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        bitmexJuneRadio = new JRadioButton("bitmex june futures", instruments.contains("bitmexJune"));
        futuresPanel.add(bitmexJuneRadio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        bitmexSeptRadio = new JRadioButton("bitmex september futures", instruments.contains("bitmexSept"));
        futuresPanel.add(bitmexSeptRadio, gbc);



        gbc.gridx = 0;
        gbc.gridy = 2;
        okexThisWeekRadio = new JRadioButton("okex this week futures", instruments.contains("okexThisWeek"));
        futuresPanel.add(okexThisWeekRadio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        okexNextWeekRadio = new JRadioButton("okex next week futures", instruments.contains("okexNextWeek"));
        futuresPanel.add(okexNextWeekRadio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        okexQuarterlyRadio = new JRadioButton("okex quarterly futures", instruments.contains("okexQuarterly"));
        futuresPanel.add(okexQuarterlyRadio, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
//        gbc.anchor = GridBagConstraints.CENTER;
        settingsPanel.add(futuresPanel, gbc);




        JPanel tradeSizePanel = new JPanel(new GridBagLayout());
        tradeSizePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.white, 0), "trade size"));



        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel minLabel = new JLabel("minimum");
        tradeSizePanel.add(minLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField minimumAmount = new JTextField(Formatter.amountFormat(minimumTradeAmt), 5);
        tradeSizePanel.add(minimumAmount, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel maxLabel = new JLabel("maximum");
        tradeSizePanel.add(maxLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField maxAmount = new JTextField(maxTradeAmt == 999999999 ? "∞" : Formatter.amountFormat(maxTradeAmt), 5);
        tradeSizePanel.add(maxAmount, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        settingsPanel.add(tradeSizePanel, gbc);




        JPanel setPanel = new JPanel(new GridBagLayout());
        setPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.white, 0), "settings"));





        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JRadioButton alwaysOnTopRadio = new JRadioButton("always on top");
        alwaysOnTopRadio.setSelected(alwaysOnTop);
        setPanel.add(alwaysOnTopRadio, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        JRadioButton hideFrameRadio = new JRadioButton("hide frame");
        hideFrameRadio.setSelected(hideFrame);
        setPanel.add(hideFrameRadio, gbc);



        gbc.gridx = 0;
        gbc.gridy = 3;
        settingsPanel.add(setPanel, gbc);




        int result = JOptionPane.showConfirmDialog(null, settingsPanel, "config", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {

            //ok set settings

            setAlwaysTop(alwaysOnTopRadio.isSelected());

            setShowFrame(hideFrameRadio.isSelected());

            setMinimumAmt(minimumAmount.getText());

            setMaximumAmount(maxAmount.getText());

            setInstruments();


        }
    }

    private void setInstruments() {

        setInstrumentBool(bitmexPerpSwapRadio, "bitmexPerp");
        setInstrumentBool(bitfinexSpotRadio, "bitfinexSpot");
        setInstrumentBool(okexSpotRadio, "okexSpot");
        setInstrumentBool(gdaxSpotRadio, "gdaxSpot");
        setInstrumentBool(binanceSpotRadio, "binanceSpot");

        setInstrumentBool(bitmexJuneRadio, "bitmexJune");
        setInstrumentBool(bitmexSeptRadio, "bitmexSept");

        setInstrumentBool(okexThisWeekRadio, "okexThisWeek");
        setInstrumentBool(okexNextWeekRadio, "okexNextWeek");
        setInstrumentBool(okexQuarterlyRadio, "okexQuarterly");




    }
    private void setInstrumentBool(JRadioButton radio, String ins) {

        if (radio.isSelected() && !instruments.contains(ins)) {

            instruments.add(ins);

        } else if (!radio.isSelected() && instruments.contains(ins)) {

            if (instruments.contains(ins)) {
                instruments.remove(ins);
            }
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


    @Override
    public void receiveBroadcast(String message) throws InterruptedException, IOException {

        boolean side = (message.substring(message.indexOf("!")+1, message.indexOf("!#")).contains("Buy"));
        String exchange = message.substring(message.indexOf("%") + 1, message.indexOf("%<"));
        String instrument = message.substring(message.indexOf("<") + 1, message.indexOf(">!"));
        System.out.println(instrument);
        System.out.println("in array: " + instruments.toString());
        final int size = Integer.parseInt(message.substring(message.indexOf("#") + 1, message.indexOf("#@")));

        System.out.println(instruments.contains(instrument));


        if (Math.abs(size) >= minimumTradeAmt && Math.abs(size) <= maxTradeAmt && instruments.contains(instrument)) {
            EventQueue.invokeLater(() -> {

                orders.add(0, new MarketOrder(exchange, size, 5));

                if (orders.size() > 100) {
                    orders.remove(orders.size() - 1);
                    orders.remove(orders.size() - 1);
                    orders.remove(orders.size() - 1);
                }

                //maybe remove some of these
                revalidate();
                tradesTable.revalidate();
                tradesScrollPane.revalidate();
            });
        }


//        if (message.contains("bitmex")) {
//
//            System.out.println(message);
//
//            boolean side = (message.substring(message.indexOf("!")+1, message.indexOf("!#")).contains("Buy"));
//            String instrument = message.substring(message.indexOf("<")+1, message.indexOf(">!"));
//            System.out.println(instrument);
//            System.out.println("in array: " + instruments.toString());
//            final int size = Integer.parseInt(message.substring(message.indexOf("#") + 1, message.indexOf("#@")));
//
//            System.out.println(instruments.contains(instrument));
//
//
//            if (Math.abs(size) >= minimumTradeAmt && Math.abs(size) <= maxTradeAmt && instruments.contains(instrument)) {
//                EventQueue.invokeLater(() -> {
//
//                    orders.add(0, new MarketOrder("bitmex", size, 5));
//
//                    if (orders.size() > 100) {
//                        orders.remove(orders.size()-1);
//                        orders.remove(orders.size()-1);
//                        orders.remove(orders.size()-1);
//                    }
//
//                    //maybe remove some of these
//                    revalidate();
//                    tradesTable.revalidate();
//                    tradesScrollPane.revalidate();
//                });
//            }
//
//        } else if (message.contains("bitmexliq")) {
//
////            Toolkit.getDefaultToolkit().beep();
//
//            boolean side = (message.substring(message.indexOf("!"), message.indexOf("!#")).contains("Buy"));
//            double amount = Double.parseDouble(message.substring(message.indexOf("#") + 1, message.indexOf("#@")));
//            System.out.println("liq amount: " + amount);
//
//            double price = Double.parseDouble(message.substring(message.indexOf("@") + 1, message.indexOf("@*")));
//            String action = String.valueOf(message.substring(message.indexOf("*") + 1, message.indexOf("*^")));
//
//            String id = String.valueOf(message.substring(message.indexOf("^") + 1, message.indexOf("^_")));
//
////            if (action.contains("insert")) {
////                addLiq(new TradeUni("bitmex", Formatter.kFormat(amount, 0) + "", amount, side, price, "time", id));
////            } else if (action.contains("update")) {
////                updateLiq(new TradeUni("bitmex", "", amount, side, price, "time", id));
//        } else if (message.contains("bitfinex")) {
//
//
//            System.out.println(message);
//            boolean side = (message.substring(message.indexOf("!"), message.indexOf("!#")).contains("true"));
//            final int size = Integer.parseInt(message.substring(message.indexOf("#") + 1, message.indexOf("#@")));
//
//            System.out.println("new bitfinex trade " + size);
//
//            if (Math.abs(size) >= minimumTradeAmt && Math.abs(size) <= maxTradeAmt) {
//
//                EventQueue.invokeLater(() -> {
//
//                    orders.add(0, new MarketOrder("bitfinex", size, 5));
//
//                    //maybe remove some of these
//                    revalidate();
//                    tradesTable.revalidate();
//                    tradesScrollPane.revalidate();
//                });
//            }
//
//
//        } else if (message.contains("bitmex") || message.contains("okex") || message.contains("binance") || message.contains("gdax")) {
////            addTradeData(message, message.substring(0, 1).equals("u"));
//
//        }
    }

    public void setMinimumAmt(String minimumAmt) {
        try {
            String minString = minimumAmt.replaceAll("\\D", "");

            int min = Integer.parseInt(minString);
            this.minimumTradeAmt = min;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMaximumAmount(String maximumAmount) {

        if (!maximumAmount.equals("∞") && !maximumAmount.equals("0")) {
            try {

                String maxString = maximumAmount.replaceAll("\\D", "");

                int max = Integer.parseInt(maxString);
                this.maxTradeAmt = max;
            } catch (Exception e) {
                maxTradeAmt = 999999999;
                e.printStackTrace();
            }
        }


    }
}
