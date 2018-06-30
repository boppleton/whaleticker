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
                            if (this.getName().contains("mex")) {
                                System.out.println("disconnecting bitmex..");
                                LaunchWindow.connectBitmex(false);

                                System.out.println("pausing bitmex..");
                                Thread.sleep(4000 + rand.nextInt(2000));

                                System.out.println("connecting bitmex..");
                                LaunchWindow.connectBitmex(true);
                            } else if (this.getName().contains("finex")) {
                                LaunchWindow.connectBitfinex(false);
                                Thread.sleep(4000 + rand.nextInt(2000));
                                LaunchWindow.connectBitfinex(true);
                            }else if (this.getName().contains("kex")) {
                                System.out.println("disconnecting okex..");
                                LaunchWindow.connectOkex(false);

                                System.out.println("puasing okex..");
                                Thread.sleep(4000 + rand.nextInt(2000));

                                System.out.println("connecting okex..");
                                LaunchWindow.connectOkex(true);
                            }else if (this.getName().contains("dax")) {
                                LaunchWindow.connectGdax(false);
                                Thread.sleep(4000 + rand.nextInt(2000));
                                LaunchWindow.connectGdax(true);
                            }else if (this.getName().contains("nance")) {
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