package gui;

import gui.market.MarketWindow;
import websocket.Broadcaster;
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

    private JRadioButton bitmexConnectRadio;
    private JRadioButton bitfinexConnectRadio;
    private JRadioButton okexConnectRadio;
    private JRadioButton gdaxConnectRadio;
    private JRadioButton binanceConnectRadio;

    private GridBagConstraints gbc;
    private Container c;
    private JPanel connectionsPan;

    public LaunchWindow(String title) throws URISyntaxException, InterruptedException {
        super(title);


        Buncher.startUpdateThread();

        setLayout(new GridBagLayout());

        gbc = new GridBagConstraints();
        c = getContentPane();

        connectionsPan = new JPanel(new GridBagLayout());
        connectionsPan.setBorder(BorderFactory.createTitledBorder("exchanges"));

        setupMarketButton();

        setupLiqsButton();

//        setupOrderbookButton();

        setupConnectionRadios();




    }

    private void setupConnectionRadios() {

        gbc.gridx = 0;
        gbc.gridy = 2;
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

        binanceConnectRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    connectBinance(binanceConnectRadio.isSelected());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

    }


    public void connectBitmex(boolean connect) throws URISyntaxException, InterruptedException {

        if (connect) {


            try {

                Thread thread = new Thread(() -> {
                    try {
                        bitmexclient = new BitmexClient();
                        bitmexclient.connectBlocking();
                        bitmexclient.setConnectionLostTimeout(10);
                        bitmexclient.subscribe(true, "trade", "XBTUSD");
                        bitmexclient.subscribe(true, "trade", "XBTM18");
                        bitmexclient.subscribe(true, "trade", "XBTU18");
                        bitmexclient.subscribe(true, "liquidation", "XBTUSD");

                    } catch(Exception v) {
                        v.printStackTrace();
                        System.out.println("loop error!");
                    }
                });
                thread.start();


                Thread.sleep(5000);




                if (bitmexclient.isOpen()) {
                    bitmexConnectRadio.setText("bitmex: connected");
                } else {
                    bitmexConnectRadio.setSelected(false);
                    bitmexConnectRadio.setText("bitmex: failed connection");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            bitmexclient.close();
            bitmexclient = null;
            bitmexConnectRadio.setText("bitmex: disconnected");
        }


    }

    public void connectBitfinex(boolean connect) throws URISyntaxException, InterruptedException {


        if (connect) {
            try {

                Thread thread = new Thread(() -> {
                    try {
                        bitfinexClient = new BitfinexClient();
                        bitfinexClient.connectBlocking();
                        bitfinexClient.subscribe(true, "trades", "BTCUSD");

                    } catch(Exception v) {
                        v.printStackTrace();
                        System.out.println("loop error!");
                    }
                });
                thread.start();


                Thread.sleep(5000);



                if (bitfinexClient.isOpen()) {
                    bitfinexConnectRadio.setText("bitfinex: connected");
                } else {
                    bitfinexConnectRadio.setSelected(false);
                    bitfinexConnectRadio.setText("bitfinex: failed connection");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            bitfinexClient.close();
            bitfinexClient = null;
            bitfinexConnectRadio.setText("bitfinex: disconnected");
        }
    }

    public void connectOkex(boolean connect) throws URISyntaxException, InterruptedException {

        if (connect) {

            try {

                Thread thread = new Thread(() -> {
                    try {
                        okexClient = new OkexClient();
                        okexClient.connectBlocking();
                        okexClient.send("{'event':'addChannel','channel':'ok_sub_spot_btc_usdt_deals'}");
                        okexClient.send("{'event':'addChannel','channel':'ok_sub_futureusd_btc_trade_this_week'}");
                        okexClient.send("{'event':'addChannel','channel':'ok_sub_futureusd_btc_trade_next_week'}");
                        okexClient.send("{'event':'addChannel','channel':'ok_sub_futureusd_btc_trade_quarter'}");

                    } catch(Exception v) {
                        v.printStackTrace();
                        System.out.println("loop error!");
                    }
                });
                thread.start();


                Thread.sleep(5000);



                if (okexClient.isOpen()) {
                    okexConnectRadio.setText("okex: connected");
                } else {
                    okexConnectRadio.setSelected(false);
                    okexConnectRadio.setText("okex: failed connection");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            okexClient.close();
            okexClient = null;
            okexConnectRadio.setText("okex: disconnected");

        }
    }

    public void connectGdax(boolean connect) throws URISyntaxException, InterruptedException {

        if (connect) {

            try {

                Thread thread = new Thread(() -> {
                    try {
                        gdaxClient = new GdaxClient();
                        gdaxClient.connectBlocking();
                        gdaxClient.subscribe(true, "", "");

                    } catch(Exception v) {
                        v.printStackTrace();
                        System.out.println("loop error!");
                    }
                });
                thread.start();


                Thread.sleep(5000);



                if (gdaxClient.isOpen()) {
                    gdaxConnectRadio.setText("gdax: connected");
                } else {
                    gdaxConnectRadio.setSelected(false);
                    gdaxConnectRadio.setText("gdax: failed connection");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            gdaxClient.close();
            gdaxClient = null;
            gdaxConnectRadio.setText("gdax: disconnected");
        }

    }

    public void connectBinance(boolean connect) throws URISyntaxException, InterruptedException {

        if (connect) {

            try {

                Thread thread = new Thread(() -> {
                    try {
                        binanceClient = new BinanceClient("btcusdt@aggTrade");
                        binanceClient.connectBlocking();

                    } catch(Exception v) {
                        v.printStackTrace();
                        System.out.println("loop error!");
                    }
                });
                thread.start();


                Thread.sleep(5000);

                binanceClient = new BinanceClient("btcusdt@aggTrade");
                binanceClient.connectBlocking();

                if (binanceClient.isOpen()) {
                    binanceConnectRadio.setText("binance: connected");
                } else {
                    binanceConnectRadio.setSelected(false);
                    binanceConnectRadio.setText("binance: failed connection");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            binanceClient.close();
            binanceClient = null;
            binanceConnectRadio.setText("binance: disconnected");
        }
    }


    private void setupLiqsButton() {

        //liqs button
        JButton liqsButton = new JButton("mex liquidations");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 1;
        c.add(liqsButton, gbc);

    }

    private void setupOrderbookButton() {

        //orderbook button
        JButton orderbookButton = new JButton("limit orders");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weighty = 1;
        c.add(orderbookButton, gbc);

    }

    private void setupMarketButton() {

        //market orders button
        JButton marketButton = new JButton("market orders");
        marketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MarketWindow marketWindow = new MarketWindow("");
                marketWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //set X close
                marketWindow.setSize(120, 400); //set dimensions
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
}
