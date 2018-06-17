package websocket.exchange.binance;

import org.java_websocket.handshake.ServerHandshake;
import websocket.Buncher;
import websocket.Client;
import websocket.TradeUni;
import websocket.exchange.binance.dto.AggTrade;

import java.net.URI;
import java.net.URISyntaxException;

public class BinanceClient extends Client {

    private static Buncher buncher = new Buncher();

    private static double lastPrice;



    public BinanceClient(String stream) throws URISyntaxException {
        super(new URI("wss://stream.binance.com:9443/ws/" + stream));
    }


    @Override
    public void onMessage(String message) {

        if (message.contains("\"e\":\"aggTrade\",")) {
            onMessageTrade(message);
//        } else if (message.contains("orderBookL2")) {
////            onMessageOrderBook(message);
//        } else if (message.contains("liquidation")) {
////            onMessageLiq(message);
        } else {
            onMessageOther(message);
        }

//            System.out.println(message);


    }


    private void onMessageTrade(String message) {

        AggTrade aggTrade = null;

        try {

//            System.out.println("\n" + message);

            aggTrade = mapper.readValue(message, AggTrade.class);

//            System.out.println(aggTrade.getQuantity());

//            double total = Double.parseDouble(aggTrade.getQuantity());


            TradeUni t = new TradeUni("binance", "binanceSpot", (Double.parseDouble(aggTrade.getQuantity())*Double.parseDouble(aggTrade.getPrice())), aggTrade.getSide(), Double.parseDouble(aggTrade.getPrice()), aggTrade.getEventTime().toString(), aggTrade.getAggId().toString());


            buncher.addToBuncher(t);

            if (t.getPrice() != lastPrice) {
//                System.out.println("binance adding last price " + t.getPrice());
//                MainView.binanceLastPrice = t.getPrice();
                lastPrice = t.getPrice();
            }

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
        System.out.println("binance onOpen");
        super.onOpen(handshakedata);
    }
}
