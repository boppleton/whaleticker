package websocket.exchange.bitmex;

import org.java_websocket.handshake.ServerHandshake;
import websocket.Broadcaster;
import websocket.Buncher;
import websocket.Client;
import websocket.exchange.bitmex.book.BitmexBook;
import websocket.exchange.bitmex.dto.book.BitmexBookBase;
import websocket.exchange.bitmex.dto.book.BitmexBookData;
import websocket.exchange.bitmex.dto.liq.BitmexLiqBase;
import websocket.exchange.bitmex.dto.liq.BitmexLiqData;
import websocket.exchange.bitmex.dto.trade.BitmexTrade;
import websocket.exchange.bitmex.dto.trade.BitmexTrades;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


public class BitmexClient extends Client {


    private static BitmexBook book = new BitmexBook();

    private static double lastPrice;


    public BitmexClient() throws URISyntaxException {
        super(new URI("wss://www.bitmex.com/realtime/"));
    }

    public void subscribe(boolean connect, String topic, String pair) {
        send("{\"op\": \""   +(connect ? "" : "un")+   "subscribe\", \"args\": [\"" +topic+ ":" +pair+ "\"]}");
    }

    @Override
    public void onMessage(String message) {

        if (message.contains("\"table\":\"trade\",\"action\":\"insert\"")) {
            onMessageTrade(message);
        } else if (message.contains("orderBookL2")) {
            try {
                onMessageOrderBook(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (message.contains("liquidation")) {
            try {
                onMessageLiq(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
//        } else {
//            onMessageOther(message);
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


            if (bookBase.getAction().contains("insert")) {
                book.insert(bookData.getPrice(), bookData.getSize(), bookData.getId(), bookData.getSide().contains("Buy"));
            } else if (bookBase.getAction().contains("update")) {
                book.update(bookData.getSize(), bookData.getId(), bookData.getSide().contains("Buy"));
            } else if (bookBase.getAction().contains("delete")) {
                book.delete(bookData.getId(), bookData.getSide().contains("Buy"));
            }


        }



    }

    //
    private void onMessageTrade(String message) {

//        System.out.println(message);

        BitmexTrades trades;

        try {

            //json mappin
            trades = mapper.readValue(message, BitmexTrades.class);
            List<BitmexTrade> tradeData = trades.getData();

            //ez
            int total = 0;
            for (int i = 0; i < tradeData.size(); i++) {
                total += tradeData.get(i).getSize();
            }
            double firstPrice = tradeData.get(0).getPrice();
            double lasttPrice = tradeData.get(tradeData.size()-1).getPrice();

            if (total >= 100) {
                Broadcaster.broadcast( "%" + "bitmex" + "%<" + getInstrument(tradeData.get(0)) + ">!" + (tradeData.get(0).getSide().equals("Buy")) + "!#" + (tradeData.get(0).getSide().equals("Sell") ? "-" : "") + total + "#@" + tradeData.get(0).getPrice() + "@*" + tradeData.get(0).getTimestamp() + "*~" + firstPrice + "~=" + lasttPrice + "=");
            }

            lastPrice = tradeData.get(tradeData.size()-1).getPrice();

//            if (tradeData.get(0).getSymbol().equals("XBTUSD") && lasttPrice > 0) {
////                System.out.println("bitmex adding last price " + tradeData.get(tradeData.size() - 1).getPrice());
//                MainView.bitmexLastPrice = lastPrice;
//
//            }



            //bunch
//            for (BitmexTrade trade : tradeData) {
//                TradeUni t = new TradeUni("bitmex", "perp", trade.getSize(), trade.getSide().equals("Buy"), trade.getPrice(), String.valueOf(trade.getTimestamp()), trade.getTrdMatchID());
//                buncher.addToBuncher(t);
//            }

        } catch (Exception ee) {
            System.out.println(ee.getLocalizedMessage());
            System.out.println(message);
        }

    }

        private String getInstrument(BitmexTrade trade) {
        String instrument;

//        System.out.println(trade.getSymbol());

        switch (trade.getSymbol()) {
            case "XBTUSD":
                instrument = "bitmexPerp";
                break;
            case "XBTM18":
                instrument = "bitmexJune";
                break;
            case "XBTU18":
                instrument = "bitmexSept";
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

            Broadcaster.broadcast("(bitmexliq)!" + (liqData.getSide() == null ? -1 : liqData.getSide()) + "!#" + (liqData.getLeavesQty() == null ? -1 : liqData.getLeavesQty().doubleValue())  + "#@" + (liqData.getPrice() == null ? -1 : liqData.getPrice())  + "@*" + liqBase.getAction() + "*^" + liqData.getOrderID() + "^_");



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onMessageOther(String message) {
        System.out.println(message);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("bitmex onOpen");
        super.onOpen(handshakedata);
    }
}