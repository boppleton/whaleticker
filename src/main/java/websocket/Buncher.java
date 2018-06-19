package websocket;


import java.util.HashMap;
import java.util.Random;

public class Buncher {

    //trade stream variables
    private static TradeUni bunch = null;

    private static Thread thread;

    private static HashMap<String, String> msgs = new HashMap<>();

    private static int minAmount = 100;

    public Buncher() {

    }

    public static void startUpdateThread() {

        thread = new Thread(() -> {
            try {
                for (;;) {
                    msgs.forEach((key, value) -> Broadcaster.broadcast(msgs.get(key)));
                    msgs.clear();
                    Thread.sleep( 500);
                }
            } catch(Exception v) {
                v.printStackTrace();
                System.out.println("loop error!");
            }
        });
        thread.start();
    }

    public void addToBuncher(TradeUni trade) {


        //if no bunch start new one
        if (bunch == null) { newBunch(trade); }

        //if there is a bunch, and this trade has same timestamp/type + any exchange specifics
        else if (trade.getTimestamp().equals(bunch.getTimestamp()) && trade.getSide() == bunch.getSide() && exchangeSpecificCondition(trade, bunch)) {

            //update trade amount
            bunch.setSize(bunch.getSize() + trade.getSize());

            //add this trade to current bunch
            if (bunch.getSize() >= minAmount) { add(bunch, trade, true); }
        }

        //if there is a bunch but old timestamp or new type, new bunch with this current incoming trade
        else { newBunch(trade); }
    }


    private void newBunch(TradeUni trade) {

        bunch = trade;

        bunch.setFirstPrice(trade.getPrice());

        if (bunch.getSize() >= minAmount) {
            add(bunch, trade, false);
        }
    }

    private void add(TradeUni bunch, TradeUni lastTrade, boolean updateExisting)  {

        bunch.setId(lastTrade.getId());

        if (lastTrade.getPrice() > 0) {
            bunch.setLastPrice(lastTrade.getPrice());
        }


        String msg = "";

        if (!updateExisting) {

            msg = "%" + bunch.getExchangeName() + "%<" + bunch.getInstrument() + ">!" + bunch.getSide() + "!#" + (int) bunch.getSize() + "#@" + bunch.getPrice() + "@*" + bunch.getTimestamp() + "*~" + Buncher.bunch.getFirstPrice() + "~=" + bunch.getLastPrice() + "=+";
            System.out.println(msg);

            msgs.put(bunch.getTimestamp(), msg);

        } else if (updateExisting) {
            msg = "%" + bunch.getExchangeName() + "%<" + bunch.getInstrument() + ">!" + bunch.getSide() + "!#" + (int) bunch.getSize() + "#@" + bunch.getPrice() + "@*" + bunch.getTimestamp() + "*~" + Buncher.bunch.getFirstPrice() + "~=" + bunch.getLastPrice() + "=+";
            System.out.println("+ " + msg);

            msgs.remove(bunch.getTimestamp());
            msgs.put(bunch.getTimestamp(), msg);

        }

        if (lastTrade.getExchangeName().contains("okex")) {
//            System.out.println(bunch.getSize() + " first price bunch: " + bunch.getFirstPrice() + " last price lasttrade: " + lastTrade.getLastPrice());
        }

    }

    private boolean exchangeSpecificCondition(TradeUni trade, TradeUni bunch) {

        if (trade.getExchangeName().equals("okex")) {

            //okex id sequence check
            return Double.parseDouble(trade.getId()) < Double.parseDouble(bunch.getId()) + 10;
        }//put other if (exchange)'s here

        return true;
    }
}
