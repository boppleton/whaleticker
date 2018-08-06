package gui;

import gui.limit.LimitWindow;
import gui.liq.LiqWindow;
import gui.market.MarketWindow;
import websocket.Buncher;
import websocket.exchange.binance.BinanceClient;
import websocket.exchange.bitfinex.BitfinexClient;
import websocket.exchange.bitmex.BitmexClient;
import websocket.exchange.gdax.GdaxClient;
import websocket.exchange.okex.OkexClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;

public class LaunchWindow extends JFrame {

    private static BitmexClient bitmexclient;
    private static BitfinexClient bitfinexClient;
    private static OkexClient okexClient;
    private static BinanceClient binanceClient;
    private static GdaxClient gdaxClient;

    private static JRadioButton bitmexConnectRadio;
    private static JRadioButton bitfinexConnectRadio;
    private static JRadioButton okexConnectRadio;
    private static JRadioButton gdaxConnectRadio;
    private static JRadioButton binanceConnectRadio;

    private GridBagConstraints gbc;
    private Container c;
    private JPanel connectionsPan;

    public LaunchWindow(String title) throws URISyntaxException, InterruptedException {
        super(title);

        ImageIcon icon = new ImageIcon(getClass().getResource("/whale2.png"));
        setIconImage(icon.getImage());


        Buncher.startUpdateThread();

        setLayout(new GridBagLayout());

        gbc = new GridBagConstraints();
        c = getContentPane();

        connectionsPan = new JPanel(new GridBagLayout());
        connectionsPan.setBorder(BorderFactory.createTitledBorder("exchanges"));

        setupMarketButton();

//        setupLimitButton();

        setupLiqsButton();




//        setupOrderbookButton();

        setupConnectionRadios();

//        connectBitmex(true);
//        connectBitfinex(true);
//        connectOkex(true);
//        connectGdax(true);
//        connectBinance(true);



    }



    private void setupConnectionRadios() {

        gbc.gridx = 0;
        gbc.gridy = 3;
        c.add(connectionsPan, gbc);

        gbc.anchor = GridBagConstraints.WEST;

        bitmexConnectRadio = new JRadioButton("bitmex: disconnected");
        gbc.gridx = 0;
        gbc.gridy = 0;
        connectionsPan.add(bitmexConnectRadio, gbc);

        bitmexConnectRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    connectBitmex(bitmexConnectRadio.isSelected());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        bitfinexConnectRadio = new JRadioButton("bitfinex: disconnected");
        gbc.gridx = 0;
        gbc.gridy = 1;
        connectionsPan.add(bitfinexConnectRadio, gbc);

        bitfinexConnectRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    connectBitfinex(bitfinexConnectRadio.isSelected());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });



        okexConnectRadio = new JRadioButton("okex: disconnected");
        gbc.gridx = 0;
        gbc.gridy = 2;
        connectionsPan.add(okexConnectRadio, gbc);

        okexConnectRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    connectOkex(okexConnectRadio.isSelected());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });



        gdaxConnectRadio = new JRadioButton("gdax: disconnected");
        gbc.gridx = 0;
        gbc.gridy = 3;
        connectionsPan.add(gdaxConnectRadio, gbc);

        gdaxConnectRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    connectGdax(gdaxConnectRadio.isSelected());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        binanceConnectRadio = new JRadioButton("binance: disconnected");
        gbc.gridx = 0;
        gbc.gridy = 4;
        connectionsPan.add(binanceConnectRadio, gbc);

        binanceConnectRadio.addActionListener(e -> {
            try {
                connectBinance(binanceConnectRadio.isSelected());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

    }


    public static void connectBitmex(boolean connect) throws URISyntaxException, InterruptedException {

        if (connect) {


            try {

                Thread thread = new Thread(() -> {
                    try {
                        bitmexclient = new BitmexClient();
                        bitmexclient.setConnectionLostTimeout(500);
                        bitmexclient.connectBlocking();

                        bitmexclient.subscribe(true, "trade", "XBTUSD");
                        bitmexclient.subscribe(true, "trade", "XBTZ18");
                        bitmexclient.subscribe(true, "trade", "XBTU18");
                        bitmexclient.subscribe(true, "liquidation", "XBTUSD");

//                        bitmexclient.subscribe(true, "orderBookL2", "XBTUSD");

                    } catch(Exception v) {
                        v.printStackTrace();
                        System.out.println("loop error!");
                    }

                    while (bitmexclient == null) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (bitmexclient.isOpen()) {
                        bitmexConnectRadio.setText("bitmex: connected");
                        bitmexConnectRadio.setSelected(true);
                    } else {
                        bitmexConnectRadio.setSelected(false);
                        bitmexConnectRadio.setText("bitmex: failed connection");
                    }



                });
                thread.start();









            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            if (bitmexclient != null) {
                bitmexclient.close();
                bitmexclient = null;
            }
            bitmexConnectRadio.setText("bitmex: disconnected");
            bitmexConnectRadio.setSelected(false);
        }


    }

    public static void connectBitfinex(boolean connect) throws URISyntaxException, InterruptedException {


        if (connect) {
            try {

                Thread thread = new Thread(() -> {
                    try {
                        bitfinexClient = new BitfinexClient();
                        bitfinexClient.setConnectionLostTimeout(500);
                        bitfinexClient.connectBlocking();
                        bitfinexClient.subscribe(true, "trades", "BTCUSD");

                    } catch(Exception v) {
                        v.printStackTrace();
                        System.out.println("loop error!");
                    }

                    while (bitfinexClient == null) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (bitfinexClient.isOpen()) {
                        bitfinexConnectRadio.setText("bitfinex: connected");
                        bitfinexConnectRadio.setSelected(true);
                    } else {
                        bitfinexConnectRadio.setSelected(false);
                        bitfinexConnectRadio.setText("bitfinex: failed connection");
                    }

                });


                thread.start();






            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            if (bitfinexClient != null) {
                bitfinexClient.close();
                bitfinexClient = null;
            }
            bitfinexConnectRadio.setText("bitfinex: disconnected");
            bitfinexConnectRadio.setSelected(false);
        }
    }

    public static void connectOkex(boolean connect) throws URISyntaxException, InterruptedException {

        if (connect) {

            try {

                Thread thread = new Thread(() -> {
                    try {
                        okexClient = new OkexClient();
                        okexClient.setConnectionLostTimeout(500);
                        okexClient.connectBlocking();
                        okexClient.send("{'event':'addChannel','channel':'ok_sub_spot_btc_usdt_deals'}");
                        okexClient.send("{'event':'addChannel','channel':'ok_sub_futureusd_btc_trade_this_week'}");
                        okexClient.send("{'event':'addChannel','channel':'ok_sub_futureusd_btc_trade_next_week'}");
                        okexClient.send("{'event':'addChannel','channel':'ok_sub_futureusd_btc_trade_quarter'}");

                    } catch(Exception v) {
                        v.printStackTrace();
                        System.out.println("loop error!");
                    }

                    while (okexClient == null) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (okexClient.isOpen()) {
                        okexConnectRadio.setText("okex: connected");
                        okexConnectRadio.setSelected(true);
                    } else {
                        okexConnectRadio.setSelected(false);
                        okexConnectRadio.setText("okex: failed connection");
                    }

                });
                thread.start();







            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            if (okexClient != null) {
                okexClient.close();
                okexClient = null;
            }
            okexConnectRadio.setText("okex: disconnected");
            okexConnectRadio.setSelected(false);

        }
    }

    public static void connectGdax(boolean connect) throws URISyntaxException, InterruptedException {

        if (connect) {

            try {

                Thread thread = new Thread(() -> {
                    try {
                        gdaxClient = new GdaxClient();
                        gdaxClient.setConnectionLostTimeout(500);
                        gdaxClient.connectBlocking();
                        gdaxClient.subscribe(true, "", "");

                    } catch(Exception v) {
                        v.printStackTrace();
                        System.out.println("loop error!");
                    }

                    while (gdaxClient == null) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (gdaxClient.isOpen()) {
                        gdaxConnectRadio.setText("gdax: connected");
                        gdaxConnectRadio.setSelected(true);
                    } else {
                        gdaxConnectRadio.setSelected(false);
                        gdaxConnectRadio.setText("gdax: failed connection");
                    }

                });
                thread.start();







            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            if (gdaxClient != null) {
                gdaxClient.close();
                gdaxClient = null;
            }
            gdaxConnectRadio.setText("gdax: disconnected");
            gdaxConnectRadio.setSelected(false);
        }

    }

    public static void connectBinance(boolean connect) throws URISyntaxException, InterruptedException {

        if (connect && binanceClient == null) {

            try {

                Thread thread = new Thread(() -> {
                    try {
                        binanceClient = new BinanceClient("btcusdt@aggTrade");
                        binanceClient.setConnectionLostTimeout(500);
                        binanceClient.connectBlocking();

                    } catch(Exception v) {
                        v.printStackTrace();
                        System.out.println("loop error!");
                    }

                    while (binanceClient == null) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (binanceClient.isOpen()) {
                        binanceConnectRadio.setText("binance: connected");
                        binanceConnectRadio.setSelected(true);
                    } else {
                        binanceConnectRadio.setSelected(false);
                        binanceConnectRadio.setText("binance: failed connection");
                    }

                });
                thread.start();



            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            if (binanceClient != null) {
                binanceClient.close();
                binanceClient = null;
            }
            binanceConnectRadio.setText("binance: disconnected");
            binanceConnectRadio.setSelected(false);
        }
    }


    private void setupLiqsButton() {

        //liqs button
        JButton liqsButton = new JButton("mex liquidations");
        liqsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LiqWindow liqWindow = new LiqWindow("liquidations");
                liqWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //set X close
                liqWindow.setSize(300, 400); //set dimensions
                liqWindow.setLocationRelativeTo(null); //null makes it open in the center
                liqWindow.setVisible(true); //show window
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 1;
        c.add(liqsButton, gbc);

    }

    private void setupLimitButton() {

        //market orders button
        JButton limitButton = new JButton("limit orders");
        limitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LimitWindow limitWindow = new LimitWindow("");
                limitWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //set X close
                limitWindow.setSize(120, 400); //set dimensions
                limitWindow.setLocationRelativeTo(null); //null makes it open in the center
                limitWindow.setVisible(true); //show window
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 1;
        c.add(limitButton, gbc);

    }

    private void setupMarketButton() {

        System.out.println("market button setup");

        //market orders button
        JButton marketButton = new JButton("market orders");
        marketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("market button pressed");
                MarketWindow marketWindow = new MarketWindow("");
                marketWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //set X close
                marketWindow.setSize(150, 400); //set dimensions
                
                marketWindow.setLocationRelativeTo(null); //null makes it open in the center
                marketWindow.setVisible(true); //show window
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 1;
        c.add(marketButton, gbc);

    }

    public void removeMinMaxClose(Component comp)
    {
        if(comp instanceof JButton)
        {
            String accName = ((JButton) comp).getAccessibleContext().getAccessibleName();
            System.out.println(accName);
            if(accName.equals("Maximize")|| accName.equals("Iconify")||
                    accName.equals("Close")) comp.getParent().remove(comp);
        }
        if (comp instanceof Container)
        {
            Component[] comps = ((Container)comp).getComponents();
            for(int x = 0, y = comps.length; x < y; x++)
            {
                removeMinMaxClose(comps[x]);
            }
        }
    }

}
