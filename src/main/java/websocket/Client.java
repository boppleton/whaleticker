package websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import gui.LaunchWindow;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.util.Random;

public abstract class Client extends WebSocketClient {

    protected ObjectMapper mapper = new ObjectMapper();

    public Client(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) { }

    @Override
    public void onMessage(String message) { }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed exchange: " + this.getClass() + "Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);

        if (remote) {
            System.out.println("reconnecting " + this.getClass());
            try {

                Thread thread = new Thread(){
                    public void run() {
                        System.out.println("reconnect thread Running");
                        Random rand = new Random();
                        try {
                            Thread.sleep(5000 + rand.nextInt(2000));
                            if (this.getClass().toString().contains("BitmexClient")) {
                                LaunchWindow.connectBitmex(false);
                                Thread.sleep(4000 + rand.nextInt(2000));
                                LaunchWindow.connectBitmex(true);
                            } else if (this.getClass().toString().contains("BitfinexClient")) {
                                LaunchWindow.connectBitfinex(false);
                                Thread.sleep(4000 + rand.nextInt(2000));
                                LaunchWindow.connectBitfinex(true);
                            }else if (this.getClass().toString().contains("OkexClient")) {
                                LaunchWindow.connectOkex(false);
                                Thread.sleep(4000 + rand.nextInt(2000));
                                LaunchWindow.connectOkex(true);
                            }else if (this.getClass().toString().contains("GdaxClient")) {
                                LaunchWindow.connectGdax(false);
                                Thread.sleep(4000 + rand.nextInt(2000));
                                LaunchWindow.connectGdax(true);
                            }else if (this.getClass().toString().contains("BinanceClient")) {
                                LaunchWindow.connectBinance(false);
                                Thread.sleep(4000 + rand.nextInt(2000));
                                LaunchWindow.connectBinance(true);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                };

                thread.start();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}