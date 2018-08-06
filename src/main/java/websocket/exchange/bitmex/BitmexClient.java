package websocket.exchange.bitmex;

import org.java_websocket.handshake.ServerHandshake;
import utils.Broadcaster;
import websocket.Client;
//import websocket.exchange.bitmex.book.BitmexBook;
import websocket.exchange.bitmex.book.BitmexBookEvent;
import websocket.exchange.bitmex.dto.book.BitmexBookBase;
import websocket.exchange.bitmex.dto.book.BitmexBookData;
import websocket.exchange.bitmex.dto.liq.BitmexLiqBase;
import websocket.exchange.bitmex.dto.liq.BitmexLiqData;
import websocket.exchange.bitmex.dto.trade.BitmexTrade;
import websocket.exchange.bitmex.dto.trade.BitmexTrades;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BitmexClient extends Client {

    int totalBids = 0;
    int totalAsks = 0;

    private static HashMap<BigInteger, BitmexBookEvent> book = new HashMap<>();

//    private static BitmexBook book = new BitmexBook();

    public BitmexClient() throws URISyntaxException {
        super(new URI("wss://www.bitmex.com/realtime/"));
    }

    public void subscribe(boolean connect, String topic, String pair) {
        send("{\"op\": \"" + (connect ? "" : "un") + "subscribe\", \"args\": [\"" + topic + ":" + pair + "\"]}");
    }

    @Override
    public void onMessage(String message) {

        if (message.contains("\"table\":\"trade\",\"action\":\"insert\"")) {
            onMessageTrade(message);

        } else if (message.contains("orderBookL2")) {
            try {
                onMessageOrderBook(message);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (message.contains("liquidation")) {
            try {
                onMessageLiq(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            onMessageOther(message);
        }

    }

    private void onMessageTrade(String message) {

        BitmexTrades trades;

        try {
            trades = mapper.readValue(message, BitmexTrades.class);
            List<BitmexTrade> tradeData = trades.getData();

            //ez
            int total = 0;
            for (int i = 0; i < tradeData.size(); i++) {
                total += tradeData.get(i).getSize();
            }
            double firstPrice = tradeData.get(0).getPrice();
            double lasttPrice = tradeData.get(tradeData.size() - 1).getPrice();

            if (total >= 100) {
                Broadcaster.broadcast("%" + "bitmex" + "%<" + getInstrument(tradeData.get(0)) + ">!" + (tradeData.get(0).getSide().equals("Buy")) + "!#" + (tradeData.get(0).getSide().equals("Sell") ? "-" : "") + total + "#@" + tradeData.get(0).getPrice() + "@*" + tradeData.get(0).getTimestamp() + "*~" + firstPrice + "~=" + lasttPrice + "=+");
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
                instrument = "bitmexPerp";
                break;
            case "XBTZ18":
                instrument = "bitmexDec";
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

    private void onMessageLiq(String message) {

        if (message.contains("\"keys\":[\"orderID\"]") || message.contains("\"success\":true,\"subscribe\"")) {
//            System.out.println(message);
            return;
        }

        try {
            BitmexLiqBase liqBase = mapper.readValue(message, BitmexLiqBase.class);

            BitmexLiqData liqData = liqBase.getData().get(0);

//            System.out.println("new liq trade: " + liqBase.getAction() + " id:" + liqData.getOrderID() + " leavesQty: " + liqData.getLeavesQty() + " price: " + liqData.getPrice() + "side: " + liqData.getSide());

            Broadcaster.broadcast("(bitmexliq)!" + (liqData.getSide() == null ? -1 : liqData.getSide()) + "!#" + (liqData.getLeavesQty() == null ? -1 : liqData.getLeavesQty().doubleValue()) + "#@" + (liqData.getPrice() == null ? -1 : liqData.getPrice()) + "@*" + liqBase.getAction() + "*^" + liqData.getOrderID() + "^_");


        } catch (Exception e) {
            e.printStackTrace();
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
//            System.out.println("new book order:" + bookBase.getAction() + " side:" + bookData.getSide() + " size:" + bookData.getSize() + " price:" + bookData.getPrice() + " id: " + bookData.getId());

            BitmexBookEvent bookEvent = new BitmexBookEvent(bookBase.getAction(), bookData.getSide(), bookData.getSize() == null ? -1 : bookData.getSize(), bookData.getPrice() == null ? -1 : bookData.getPrice(), bookData.getId());

//            System.out.println("new book event: " + bookEvent.toString());

            //if ID doesnt exist in book, add this event to book
            if (!book.containsKey(bookEvent.getId())) {
                book.put(bookEvent.getId(), bookEvent);
            }

            //if id does exist, update it
            if (book.containsKey(bookEvent.getId())) {

                //get event at current ID
                BitmexBookEvent eventAtId = book.get(bookEvent.getId());

                eventAtId.setSide(bookEvent.getSide());

                //check for amt added
                if (bookEvent.getSize() - eventAtId.getSize() >= 100) {
                    if (eventAtId.getPrice() > 6000 && eventAtId.getPrice() < 7000) {
//                        System.out.println(message);
//                        System.out.println("limit added: " + bookEvent.getSide() + " " + (bookEvent.getSize() - eventAtId.getSize()) + " @ " + eventAtId.getPrice());
//
                        Broadcaster.broadcast("book" + "%" + "bitmex" + "%<" + "bitmexPerp" + ">!" + (bookEvent.getSide().equals("Buy")) + "!#" + (bookEvent.getSide().equals("Sell") ? "-" : "") + Math.abs(bookEvent.getSize() - eventAtId.getSize()) + "#@" + eventAtId.getPrice() + "@*" + "timestamp" + "*~" + "0" + "~=" + "1" + "=+");
                    }
//                } else if (bookEvent.getSize() - eventAtId.getSize() <= 100) {
//                    if (eventAtId.getPrice() > 6000 && eventAtId.getPrice() < 7000) {
//                        System.out.println(message);
//                        System.out.println("limit added: " + bookEvent.getSide() + " " + (bookEvent.getSize() - eventAtId.getSize()) + " @ " + eventAtId.getPrice());
//
//                        Broadcaster.broadcast("book" + "%" + "bitmex" + "%<" + "bitmexPerp" + ">!" + (bookEvent.getSide().equals("Sell")) + "!#" + (bookEvent.getSide().equals("Buy") ? "-" : "") + -Math.abs(bookEvent.getSize() - eventAtId.getSize()) + "#@" + eventAtId.getPrice() + "@*" + "timestamp" + "*~" + "0" + "~=" + "1" + "=+");
//                    }

                }



                if (bookEvent.getSize() != -1) {
                    eventAtId.setSize(bookEvent.getSize());
                }

                if (bookEvent.getPrice() != -1) {
                    eventAtId.setPrice(bookEvent.getPrice());
                }



                book.replace(eventAtId.getId(), eventAtId);
            }

        }

        totalBids = 0;
        totalAsks = 0;

        book.forEach(this::addTotalsPrint);

//        System.out.println("total bids: " + totalBids + " total asks: " + totalAsks);

    }

    private void addTotalsPrint(BigInteger key, BitmexBookEvent bookevent) {



        if (bookevent.getSide().contains("Buy")) {
            totalBids += bookevent.getSize();
        } else {
            totalAsks += bookevent.getSize();
        }



    }


    private void onMessageOther(String message) {

//        System.out.println(message);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("bitmex onOpen()");

        startPingLoop();

        super.onOpen(handshakedata);
    }

    private static Thread thread;

    private void startPingLoop() {

        thread = new Thread(() -> {

            for (; ; ) {

                try {
                    Thread.sleep(9000);
                    if (isOpen()) {
                        send("{'event':'ping'}");
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