package websocket.exchange.gdax;

import org.java_websocket.handshake.ServerHandshake;
import utils.Broadcaster;
import websocket.Buncher;
import websocket.Client;
import websocket.TradeUni;
//import websocket.exchange.bitmex.book.BitmexBook;
import websocket.exchange.bitmex.dto.book.BitmexBookBase;
import websocket.exchange.bitmex.dto.book.BitmexBookData;
import websocket.exchange.bitmex.dto.liq.BitmexLiqBase;
import websocket.exchange.bitmex.dto.liq.BitmexLiqData;
import websocket.exchange.bitmex.dto.trade.BitmexTrade;
import websocket.exchange.gdax.dto.GdaxMatch;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class GdaxClient extends Client {


    private static Buncher buncher = new Buncher();

//    private static BitmexBook book = new BitmexBook();

    public static int minSize = 1000;

    private static double lastPrice;

    public GdaxClient() throws URISyntaxException {
        super(new URI("wss://ws-feed.gdax.com/"));
    }

    public void subscribe(boolean connect, String topic, String pair) {
        send("{\"type\": \"subscribe\", \"product_ids\": [\"BTC-USD\"],\"channels\": [\"matches\"]}");
    }




//    {"type": "subscribe", "product_ids": ["BTC-USD"],"channels": ["matches"]}




    @Override
    public void onMessage(String message) {

//        System.out.println(message);
//
        if (message.contains("\"type\":\"match\"")) {
//            System.out.println("trade-");
            onMessageTrade(message);

//        else if (message.contains("orderBookL2")) {
//            try {
//                onMessageOrderBook(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else if (message.contains("liquidation")) {
//            try {
//                onMessageLiq(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        } else {
            onMessageOther(message);
        }

    }

    private void onMessageOrderBook(String message) throws IOException {

        if (message.contains("\"success\":true,\"subscribe\":\"orderBookL2")) {
            System.out.println("subscribed to bitmex orderbookl2");
            System.out.println(message);
            return;
        } else if (message.contains("\"orderBookL2\",\"keys\"")) {
            System.out.println("bitmex book initial push");
            System.out.println(message);
            return;
        }

        //json mappin
        BitmexBookBase bookBase = mapper.readValue(message, BitmexBookBase.class);


        for (BitmexBookData bookData : bookBase.getData()) {
            System.out.println("new book order:" + bookBase.getAction() + " side:" + bookData.getSide() + " size:" + bookData.getSize() + " price:" + bookData.getPrice() + " id: " + bookData.getId());


//            if (bookBase.getAction().contains("insert")) {
//                book.insert(bookData.getPrice(), bookData.getSize(), bookData.getId(), bookData.getSide().contains("Buy"));
//            } else if (bookBase.getAction().contains("update")) {
//                book.update(bookData.getSize(), bookData.getId(), bookData.getSide().contains("Buy"));
//            } else if (bookBase.getAction().contains("delete")) {
//                book.delete(bookData.getId(), bookData.getSide().contains("Buy"));
//            }


        }



    }

    //
    private void onMessageTrade(String message) {

//        System.out.println(message);

        GdaxMatch match;

        try {

            //json mappin
            match = mapper.readValue(message, GdaxMatch.class);


//            //ez
//            int total = 0;
//            for (int i = 0; i < tradeData.size(); i++) { total += tradeData.get(i).getSize(); }
//            if (total >= minSize) {
//                Broadcaster.broadcast( "%" + "bitmex" + "%<" + "xbtusd perp" + ">!" + (tradeData.get(0).getSide().equals("Buy")) + "!#" + total + "#@" + tradeData.get(0).getPrice() + "@*" + tradeData.get(0).getTimestamp() + "*~" +tradeData.get(0).getPrice() + "~=" + tradeData.get(0).getPrice() + "=");
//            }

//            bunch

//            System.out.println(match.getSize());

                TradeUni t = new TradeUni("gdax", "gdaxSpot", match.getSize(), match.getSide().contains("sell"), Double.parseDouble(match.getPrice()), String.valueOf(match.getTime()), match.getTradeId().toString());



                buncher.addToBuncher(t);

            if (t.getPrice() != lastPrice) {
//                System.out.println("gdax adding last price " + t.getPrice());
//                MainView.gdaxLastPrice = t.getPrice();
                lastPrice = t.getPrice();
            }


        } catch (Exception ee) {
            System.out.println(ee.getLocalizedMessage());
            System.out.println(message);
        }

    }

    private String getInstrument(BitmexTrade trade) {
        String instrument;

        switch (trade.getSymbol()) {
            case "XBTUSD":
                instrument = "Perp Swap Contracts";
                break;
            case "XBTM18":
                instrument = "June Futures";
                break;
            case "XBTU18":
                instrument = "September Futures";
                break;
            default:
                instrument = "invalid instrument";
                break;
        }

        return instrument;

    }

    private void onMessageLiq(String message) throws IOException {

//        System.out.println(message);


        if (message.contains("\"keys\":[\"orderID\"]") || message.contains("\"success\":true,\"subscribe\"")) {
            System.out.println(message);
            return;
        }

        try {

            //json mappin
            BitmexLiqBase liqBase = mapper.readValue(message, BitmexLiqBase.class);

            BitmexLiqData liqData = liqBase.getData().get(0);

            System.out.println("new liq trade: " + liqBase.getAction() + " id:" + liqData.getOrderID() + " leavesQty: " + liqData.getLeavesQty() + " price: " + liqData.getPrice() + "side: " + liqData.getSide());

            Broadcaster.broadcast("(bitmexliq XBTUSD)!" + liqData.getSide() + "!#" + liqData.getLeavesQty().doubleValue() + "#@" + liqData.getPrice() + "@*" + liqBase.getAction() + "*");



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onMessageOther(String message) {
        System.out.println(message);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("gdax onOpen");

//        startPingLoop();

        super.onOpen(handshakedata);
    }

    private static Thread thread;

    private void startPingLoop() {

        thread = new Thread(() -> {

            for (;;) {

                try {
                    Thread.sleep(9000);
                    if (isOpen()) {
                        System.out.println("sending binance ping");
                        send("ping");
                    }
//                    System.out.println("sending ping");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }


        });
        thread.start();
    }
}