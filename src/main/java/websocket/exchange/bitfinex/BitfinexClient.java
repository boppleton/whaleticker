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
        buncher.startUpdateThread();
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

//        } else if (!message.contains("te") && !message.contains("hb")) {
//            onMessageOther(message);
////        } else if (message.contains("\"addChannel\",\"data\":{\"result\":true")) {
////            System.out.println("okex channel opened: " + message);
//////            System.out.println(message);
//////        } else if (message.contains("liquidation")) {
////////            onMessageLiq(message);
//        } else {
//            onMessageOther(message);
//        }


    }


    private void onMessageTrade(String message) {

        TradeExecuted te = null;

        try {

//            System.out.println("\n\n" + message);



            te = mapper.readValue(message, TradeExecuted.class);


//            System.out.println("mssg trade: " );
//            System.out.println("startnum " + te.startNum);
//            System.out.println("te " + te.te);
//
//            System.out.println("seq " + te.seq);
//            System.out.println("timestamp " + te.timestamp);
//            System.out.println("price " + te.price);
//            System.out.println("amount " + te.amount);


            if (te.price > 0) {
//                MainView.bitfinexLastPrice = te.price;
                lastPrice = te.price;
            }

            TradeUni t = new TradeUni("bitfinex", "bitfinexSpot", (Math.abs(te.amount) * te.price), te.amount > 0, te.price, String.valueOf(te.timestamp), te.seq);


            //print, add to volume counter etc here
//            System.out.println(t.getExchangeName() + t.getSize() + (t.getSide() ? "buy" : "sell"));



            buncher.addToBuncher(t);


//
//                System.out.print(" trade- " + trades.get(i).get(1) + " amt: " + (message.contains("future") ? Double.valueOf(trades.get(i).get(2))*100 : Double.valueOf(trades.get(i).get(2))*Double.valueOf(trades.get(i).get(1))) + " type: " + trades.get(i).get(4));
//
//                System.out.printf(" %.1f", message.contains("future") ? Double.valueOf(trades.get(i).get(2))*100 : Double.valueOf(trades.get(i).get(2))*Double.valueOf(trades.get(i).get(1)));
//

//            System.out.println();


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
