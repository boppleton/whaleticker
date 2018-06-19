package websocket.exchange.bitfinex;

import org.java_websocket.handshake.ServerHandshake;
import websocket.Buncher;
import websocket.Client;
import websocket.TradeUni;
import websocket.exchange.bitfinex.dto.TradeExecuted;

import java.net.URI;
import java.net.URISyntaxException;

public class BitfinexClient extends Client {

    private static Buncher buncher = new Buncher();

    private static double lastPrice;


    public BitfinexClient() throws URISyntaxException {
        super(new URI("wss://api.bitfinex.com/ws/"));
//        buncher.startUpdateThread();
    }


    public void subscribe(boolean connect, String topic, String pair) {
        send("{\n" +
                "  \"event\": \"subscribe\",\n" +
                "  \"channel\": \"" + topic + "\",\n" +
                "  \"pair\": \"" + pair + "\"\n" +
                "}");
    }

    public void subscribeBook(boolean connect, String topic, String pair, String prec) {
        send("{\n" +
                "   \"event\":\"subscribe\",\n" +
                "   \"channel\":\"book\",\n" +
                "   \"pair\":\"BTCUSD\",\n" +
                "   \"prec\":\"R0\"\n" +
                "}");
    }


    @Override
    public void onMessage(String message) {

        if (message.contains("te")) {

            onMessageTrade(message);

        }
    }


    private void onMessageTrade(String message) {

        TradeExecuted te = null;

        try {

            te = mapper.readValue(message, TradeExecuted.class);

            if (te.price > 0) {
//                MainView.bitfinexLastPrice = te.price;
                lastPrice = te.price;
            }

            TradeUni t = new TradeUni("bitfinex", "bitfinexSpot", (Math.abs(te.amount) * te.price), te.amount > 0, te.price, String.valueOf(te.timestamp), te.seq);

            buncher.addToBuncher(t);


        } catch (Exception ee) {
            System.out.println(ee.getLocalizedMessage());
            System.out.println(message);
        }

    }


    private void onMessageOther(String message) {
        System.out.println(message);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("bitfinex onOpen()");
        super.onOpen(handshakedata);
    }
}
