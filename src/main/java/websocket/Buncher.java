package websocket;

import utils.Broadcaster;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Buncher {

    private static TradeUni bunch = null;
    private static HashMap<String, String> msgs = new HashMap<>();

    private static Thread thread;

    private static int minAmount = 100;

    public Buncher() {}

    public static void startUpdateThread() {

        thread = new Thread(() -> {

            System.out.println("new thread");

            try {
                for (;;) {
//                    System.out.println("sgs: "+ msgs);

                    Iterator it = msgs.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry)it.next();
                        Broadcaster.broadcast(pair.getValue().toString());
                        it.remove();
                    }

                    Thread.sleep(500);
                }
            } catch (Exception v) {
                v.printStackTrace();
                System.out.println("loop error!");
                thread.stop();
                thread = null;

                System.out.println("restarting thread.. ");
                startUpdateThread();
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

        String msg;

        if (!updateExisting) {

            msg = "%" + bunch.getExchangeName() + "%<" + bunch.getInstrument() + ">!" + bunch.getSide() + "!#" + (int) bunch.getSize() + "#@" + bunch.getPrice() + "@*" + bunch.getTimestamp() + "*~" + Buncher.bunch.getFirstPrice() + "~=" + bunch.getLastPrice() + "=+";

            msgs.put(bunch.getTimestamp(), msg);

        } else if (updateExisting) {
            msg = "%" + bunch.getExchangeName() + "%<" + bunch.getInstrument() + ">!" + bunch.getSide() + "!#" + (int) bunch.getSize() + "#@" + bunch.getPrice() + "@*" + bunch.getTimestamp() + "*~" + Buncher.bunch.getFirstPrice() + "~=" + bunch.getLastPrice() + "=+";

            msgs.remove(bunch.getTimestamp());
            msgs.put(bunch.getTimestamp(), msg);

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
