package gui.market;

import utils.Broadcaster;
import utils.Formatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

//todo: title bar/icon choose
//click to clear orderbook to see whats happening

public class MarketWindow extends JFrame implements Broadcaster.BroadcastListener {

    private ArrayList orders;

    private JScrollPane tradesScrollPane;
    private JTable tradesTable;

    private boolean alwaysOnTop = false;
    private boolean hideFrame = false;

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

    private JComboBox<String> titleCombo;
    private String titleExchange = "none";

    public MarketWindow(String title) {
        super(title);

        ImageIcon icon = new ImageIcon(getClass().getResource("/whale2.png"));
        setIconImage(icon.getImage());

        Broadcaster.register(this);

        orders = new ArrayList();

        setupTableScrollpane();
    }

    @Override
    public void receiveBroadcast(String message) {

        if (!message.contains("liq")) {

            final int size;

            boolean side = (message.substring(message.indexOf("!") + 1, message.indexOf("!#")).contains("true"));
            String exchange = message.substring(message.indexOf("%") + 1, message.indexOf("%<"));
            String instrument = message.substring(message.indexOf("<") + 1, message.indexOf(">!"));
            double firstPrice = Double.valueOf(message.substring(message.indexOf("~") + 1, message.indexOf("~=")));
            double lastPrice = Double.valueOf(message.substring(message.indexOf("=") + 1, message.indexOf("=+")));

            if (exchange.equals("bitmex") || side) {
                size = Integer.parseInt(message.substring(message.indexOf("#") + 1, message.indexOf("#@")));
            } else {
                size = -Integer.parseInt(message.substring(message.indexOf("#") + 1, message.indexOf("#@")));
            }

            int slipInt = (int) (lastPrice - firstPrice);

            final int slip = (!(firstPrice > 0 && lastPrice > 0) || ((size > 0 && slipInt < 0) || (size < 0 && slipInt > 0))) ? 0 : slipInt;
//

//            final int slip = (int) (lastPrice - firstPrice);
//            if (!(firstPrice > 0 && lastPrice > 0) || ((size > 0 && slip < 0) || (size < 0 && slip > 0))) {
//                slip = 0;
//            }

            setWindowTitle(instrument, lastPrice);


            if (Math.abs(size) >= minimumTradeAmt && Math.abs(size) <= maxTradeAmt && instruments.contains(instrument)) {
                EventQueue.invokeLater(() -> {

//                    System.out.println("adding " + exchange + size + side);

                    orders.add(0, new MarketOrder(exchange, instrument, size, slip, firstPrice, lastPrice));

                    if (orders.size() > 150) {
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

        }
    }

    private void setWindowTitle(String instrument, double lastPrice) {

        if (instrument.contains(titleExchange)) {
            setTitle(lastPrice + "  ");
        }

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

        //spot panel
        bitmexPerpSwapRadio = new JRadioButton("bitmex perp swaps", instruments.contains("bitmexPerp"));
        spotPanel.add(bitmexPerpSwapRadio, gbc);
        gbc.gridy = 1;
        bitfinexSpotRadio = new JRadioButton("bitfinex spot", instruments.contains("bitfinexSpot"));
        spotPanel.add(bitfinexSpotRadio, gbc);
        gbc.gridy = 2;
        okexSpotRadio = new JRadioButton("okex spot", instruments.contains("okexSpot"));
        spotPanel.add(okexSpotRadio, gbc);
        gbc.gridy = 3;
        gdaxSpotRadio = new JRadioButton("gdax spot", instruments.contains("gdaxSpot"));
        spotPanel.add(gdaxSpotRadio, gbc);
        gbc.gridy = 4;
        binanceSpotRadio = new JRadioButton("binance spot", instruments.contains("binanceSpot"));
        spotPanel.add(binanceSpotRadio, gbc);
        gbc.gridy = 0;
        settingsPanel.add(spotPanel, gbc);

        //futures
        JPanel futuresPanel = new JPanel(new GridBagLayout());
        futuresPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.white, 0), "futures"));
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        bitmexJuneRadio = new JRadioButton("bitmex june futures", instruments.contains("bitmexJune"));
        futuresPanel.add(bitmexJuneRadio, gbc);
        gbc.gridy = 1;
        bitmexSeptRadio = new JRadioButton("bitmex september futures", instruments.contains("bitmexSept"));
        futuresPanel.add(bitmexSeptRadio, gbc);
        gbc.gridy = 2;
        okexThisWeekRadio = new JRadioButton("okex this week futures", instruments.contains("okexThis"));
        futuresPanel.add(okexThisWeekRadio, gbc);
        gbc.gridy = 3;
        okexNextWeekRadio = new JRadioButton("okex next week futures", instruments.contains("okexNext"));
        futuresPanel.add(okexNextWeekRadio, gbc);
        gbc.gridy = 4;
        okexQuarterlyRadio = new JRadioButton("okex quarterly futures", instruments.contains("okexQuat"));
        futuresPanel.add(okexQuarterlyRadio, gbc);
        gbc.gridy = 1;
        settingsPanel.add(futuresPanel, gbc);

        //title
        JPanel titleComboPanel = new JPanel(new GridBagLayout());
        titleComboPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.white, 0), "title bar price"));
        titleCombo = new JComboBox<>();
        titleCombo.addItem("none");
        titleCombo.addItem("bitmexPerp");
        titleCombo.addItem("bitfinexSpot");
        titleCombo.addItem("okexSpot");
        titleCombo.addItem("gdaxSpot");
        titleCombo.addItem("binanceSpot");
        titleCombo.addItem("bitmexJune");
        titleCombo.addItem("bitmexSept");
        titleCombo.addItem("okexThis");
        titleCombo.addItem("okexNext");
        titleCombo.addItem("okexQuat");
        titleCombo.setSelectedItem(titleExchange);
        gbc.gridx = 0;
        gbc.gridy = 0;
        titleComboPanel.add(titleCombo, gbc);
        gbc.gridy = 2;
        settingsPanel.add(titleComboPanel, gbc);

        //size panel
        JPanel tradeSizePanel = new JPanel(new GridBagLayout());
        tradeSizePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.white, 0), "trade size"));
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel minLabel = new JLabel("minimum ");
        tradeSizePanel.add(minLabel, gbc);
        gbc.gridx = 1;
        JTextField minimumAmount = new JTextField(Formatter.amountFormat(minimumTradeAmt), 5);
        tradeSizePanel.add(minimumAmount, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel maxLabel = new JLabel("maximum ");
        tradeSizePanel.add(maxLabel, gbc);
        gbc.gridx = 1;
        JTextField maxAmount = new JTextField(maxTradeAmt == 999999999 ? "∞" : Formatter.amountFormat(maxTradeAmt), 5);
        tradeSizePanel.add(maxAmount, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        settingsPanel.add(tradeSizePanel, gbc);

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
        gbc.gridy = 4;
        settingsPanel.add(setPanel, gbc);

        //send it
        int result = JOptionPane.showConfirmDialog(null, settingsPanel, "settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {

            setAlwaysTop(alwaysOnTopRadio.isSelected());
            setShowFrame(hideFrameRadio.isSelected());
            setMinimumAmt(minimumAmount.getText());
            setMaximumAmount(maxAmount.getText());
            setInstruments();
            setTitleBarExchange(titleCombo.getSelectedItem().toString());
        }
    }

    private void setTitleBarExchange(String exchange) {

        setTitle("");

        titleExchange = exchange;

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
                        tradesTable.revalidate();
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
        tradesScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));

        tradesScrollPane.addMouseWheelListener(e -> {
            if (tradesScrollPane.getVerticalScrollBar().getValue() == 0) {
                tradesScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
            } else {
                tradesScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
            }
        });

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

    private void setInstruments() {

        setInstrumentBool(bitmexPerpSwapRadio, "bitmexPerp");
        setInstrumentBool(bitfinexSpotRadio, "bitfinexSpot");
        setInstrumentBool(okexSpotRadio, "okexSpot");
        setInstrumentBool(gdaxSpotRadio, "gdaxSpot");
        setInstrumentBool(binanceSpotRadio, "binanceSpot");

        setInstrumentBool(bitmexJuneRadio, "bitmexJune");
        setInstrumentBool(bitmexSeptRadio, "bitmexSept");

        setInstrumentBool(okexThisWeekRadio, "okexThis");
        setInstrumentBool(okexNextWeekRadio, "okexNext");
        setInstrumentBool(okexQuarterlyRadio, "okexQuat");
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

    private void setMinimumAmt(String minimumAmt) {
        try {
            String minString = minimumAmt.replaceAll("\\D", "");
            this.minimumTradeAmt = Integer.parseInt(minString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setMaximumAmount(String maximumAmount) {

        if (!maximumAmount.equals("∞") && !maximumAmount.equals("0") && !maximumAmount.equals("")) {
            try {
                String maxString = maximumAmount.replaceAll("\\D", "");
                this.maxTradeAmt = Integer.parseInt(maxString);
            } catch (Exception e) {
                maxTradeAmt = 999999999;
                e.printStackTrace();
            }
        }
    }
}
