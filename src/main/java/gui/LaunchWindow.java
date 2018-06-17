package gui;

import gui.market.MarketWindow;
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

    private GridBagConstraints gbc;
    private Container c;

    public LaunchWindow(String title) throws URISyntaxException, InterruptedException {
        super(title);


        connectBitmex();

//        connectBitfinex();

        setLayout(new GridBagLayout());

        gbc = new GridBagConstraints();
        c = getContentPane();

        setupMarketButton();

        setupLiqsButton();

        setupOrderbookButton();

//        setupConnectionRadios();


    }

    private void setupConnectionRadios() {

        gbc.anchor = GridBagConstraints.WEST;

        bitmexConnectRadio = new JRadioButton("bitmex: disconnected");
        bitmexConnectRadio.setSelected(bitmexclient.isOpen());
        gbc.gridx = 0;
        gbc.gridy = 3;
        c.add(bitmexConnectRadio, gbc);

        bitmexConnectRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
//                    connectBitmex(bitmexConnectRadio.isSelected());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        JRadioButton bitfinexConnectRadio = new JRadioButton("bitfinex: disconnected");
        gbc.gridx = 0;
        gbc.gridy = 4;
        c.add(bitfinexConnectRadio, gbc);

        JRadioButton okexConnectRadio = new JRadioButton("okex: disconnected");
        gbc.gridx = 0;
        gbc.gridy = 5;
        c.add(okexConnectRadio, gbc);

        JRadioButton gdaxConnectRadio = new JRadioButton("gdax: disconnected");
        gbc.gridx = 0;
        gbc.gridy = 6;
        c.add(gdaxConnectRadio, gbc);

        JRadioButton binanceConnectRadio = new JRadioButton("binance: disconnected");
        gbc.gridx = 0;
        gbc.gridy = 7;
        c.add(binanceConnectRadio, gbc);
    }

    public void connectBitmex() throws URISyntaxException, InterruptedException {

        try {
            bitmexclient = new BitmexClient();
            bitmexclient.connectBlocking();
            bitmexclient.subscribe(true, "trade", "XBTUSD");
            bitmexclient.subscribe(true, "trade", "XBTM18");
            bitmexclient.subscribe(true, "trade", "XBTU18");
            bitmexclient.subscribe(true, "liquidation", "XBTUSD");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void connectBitfinex() throws URISyntaxException, InterruptedException {

        try {
            bitfinexClient = new BitfinexClient();
            bitfinexClient.connectBlocking();
            bitfinexClient.subscribe(true, "trades", "BTCUSD");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setupLiqsButton() {

        //liqs button
        JButton liqsButton = new JButton("liquidations");
        gbc.gridx = 0;
        gbc.gridy = 2;
        c.add(liqsButton, gbc);

    }

    private void setupOrderbookButton() {

        //orderbook button
        JButton orderbookButton = new JButton("limit orders");
        gbc.gridx = 0;
        gbc.gridy = 1;
        c.add(orderbookButton, gbc);

    }

    private void setupMarketButton() {

        //market orders button
        JButton marketButton = new JButton("market orders");
        marketButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MarketWindow marketWindow = new MarketWindow("");
                marketWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //set X close
                marketWindow.setSize(100, 700); //set dimensions
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
