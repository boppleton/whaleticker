package websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;

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
                reconnectBlocking();
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