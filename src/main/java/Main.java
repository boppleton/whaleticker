import gui.LaunchWindow;
import org.pushingpixels.substance.api.skin.*;
import websocket.BroadcastListener;
import websocket.Broadcaster;
import websocket.exchange.binance.BinanceClient;
import websocket.exchange.bitfinex.BitfinexClient;
import websocket.exchange.bitmex.BitmexClient;
import websocket.exchange.gdax.GdaxClient;
import websocket.exchange.okex.OkexClient;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    private static BitmexClient bitmexclient;
    private static BitfinexClient bitfinexClient;
    private static OkexClient okexClient;
    private static BinanceClient binanceClient;
    private static GdaxClient gdaxClient;

    public static void main(String[] args) throws URISyntaxException, InterruptedException {

        startLaunchWindow();

        BroadcastListener broadcastListener = new BroadcastListener();

        bitmexclient = new BitmexClient();
        bitmexclient.connectBlocking();
        bitmexclient.subscribe(true, "trade", "XBTUSD");



    }

    private static void startLaunchWindow() {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new SubstanceMistSilverLookAndFeel());
                } catch (Exception e) {
                    System.out.println("Substance Graphite failed to initialize");
                }

                LaunchWindow launchWindow = new LaunchWindow("whaleticker");
                launchWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //set X close
                launchWindow.setSize(220, 170); //set dimensions
                launchWindow.setLocationRelativeTo(null); //null makes it open in the center
                launchWindow.setVisible(true); //show window

            }});
    }

}
