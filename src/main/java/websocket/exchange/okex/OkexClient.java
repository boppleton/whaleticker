package websocket.exchange.okex;

import org.java_websocket.handshake.ServerHandshake;
import websocket.Buncher;
import websocket.Client;
import websocket.TradeUni;
import websocket.exchange.okex.dto.Spots;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.List;
import java.util.TimeZone;

public class OkexClient extends Client {//todo: remove first pushes by checking for the .0 at the end of amt

    private static Buncher buncher = new Buncher();

    private static double lastPrice;

    private static long startTime;
    private static String startTimeString;


    public OkexClient() throws URISyntaxException {
        super(new URI("wss://real.okex.com:10440/websocket/okexapi"));
        buncher.startUpdateThread();

        startTimeSetup();
    }

    private void startTimeSetup() {
        startTime = System.currentTimeMillis();

        startTimeString = String.format("%tT", startTime- TimeZone.getTimeZone("GMT+10").getRawOffset());

        System.out.println("start time: " + startTimeString);
    }


    public void subscribe(boolean connect, String topic, String pair) {
        send("{\"op\": \"" + (connect ? "" : "un") + "subscribe\", \"args\": [\"" + topic + ":" + pair + "\"]}");
    }


    @Override
    public void onMessage(String message) {


//        System.out.println(message);

        if (message.contains("deals\",\"data\":")) {
            onMessageTrade(message);
        } else if (message.contains("futureusd_btc_trade_quarter")) {
            onMessageTrade(message);
        } else if (message.contains("futureusd_btc_trade_next_week")) {
            onMessageTrade(message);
        }else if (message.contains("futureusd_btc_trade_this_week")) {
            onMessageTrade(message);
        }

        if (message.contains("\"addChannel\",\"data\":{\"result\":true")) {
            System.out.println("okex channel opened: " + message);
//            System.out.println(message);
//        } else if (message.contains("liquidation")) {
////            onMessageLiq(message);
        } else {
//            onMessageOther(message);
        }


    }


    private void onMessageTrade(String message) {



        Spots[] spots = null;

        try {

//            System.out.println(message);

            spots = mapper.readValue(message, Spots[].class);

            List<List<String>> trades = spots[0].getData();

            LocalTime startM = LocalTime.parse(startTimeString);

            LocalTime tradeM = LocalTime.parse(trades.get(0).get(3));

            if (!message.contains("future")) {
//                MainView.okexLastPrice = Double.parseDouble(trades.get(0).get(1));
            }

            if (tradeM.isAfter(startM)) {

                for (int i = 0; i < trades.size(); i++) {

//                    System.out.println("okex trade 0:" + trades.get(i).get(0) + " 1:" + trades.get(i).get(1) +" 2:" + trades.get(i).get(2) +" 3:" + trades.get(i).get(3) +" 4:" + trades.get(i).get(4));

                    String instrument = null;

                    switch (spots[0].getChannel()) {
                        case "ok_sub_spot_btc_usdt_deals":
                            instrument = "okexSpot";
                            break;
                        case "ok_sub_futureusd_btc_trade_this_week":
                            instrument = "okexThis";
                            break;
                        case "ok_sub_futureusd_btc_trade_next_week":
                            instrument = "okexNext";
                            break;
                        case "ok_sub_futureusd_btc_trade_quarter":
                            instrument = "okexQuat";
                            break;
                        default:
                            instrument = "invalid instrument";
                            break;
                    }

                    TradeUni t = new TradeUni();
                    t.setExchangeName("okex");
                    t.setSide(trades.get(i).get(4).equals("bid"));
                    t.setPrice(Double.valueOf(trades.get(i).get(1)));
                    t.setInstrument(instrument);
                    t.setSize(message.contains("future") ? (Double.parseDouble(trades.get(i).get(2)) * 100) : Double.parseDouble(trades.get(i).get(2)) * t.getPrice());
                    t.setTimestamp(trades.get(i).get(3));
                    t.setId(trades.get(i).get(0));


                    buncher.addToBuncher(t);

//                    if (t.getPrice() != lastPrice && t.getInstrument().equals("okexSpot")) {
//                        MainView.okexLastPrice = t.getPrice();
                        lastPrice = t.getPrice();
//                    }

                }

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
        System.out.println("okex onOpen()");
        super.onOpen(handshakedata);
    }
}
