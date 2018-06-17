package websocket.exchange.bitmex.book;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.TreeMap;

public class BitmexBook {

    private static TreeMap<Double, Integer> bids = new TreeMap<>();
    private static TreeMap<Double, Integer> asks = new TreeMap<>();


    private static HashMap<BigInteger, Double> bidIds = new HashMap<>();
    private static HashMap<BigInteger, Double> askIds = new HashMap<>();




    public void insert(double price, int size, BigInteger id, boolean side) {


        if (side) {
            bids.put(price, size);
            bidIds.put(id, price);
            System.out.println(bids.toString());
        } else {
            asks.put(price, size);
            askIds.put(id, price);
            System.out.println(asks.toString());

        }
    }

    public void update(int size, BigInteger id, boolean side) {

        if (side && bidIds.get(id) != null) {
            bids.put(bidIds.get(id), size);
            System.out.println(bids.toString());
        }

        if (!side && askIds.get(id) != null){
            asks.put(askIds.get(id), size);
            System.out.println(asks.toString());
        }

    }

    public void delete(BigInteger id, boolean side) {

        if (side && bidIds.get(id) != null) {
            bids.remove(bidIds.get(id));
            System.out.println(bids.toString());
        }

        if (!side && askIds.get(id) != null){
            asks.remove(askIds.get(id));
            System.out.println(asks.toString());
        }
    }


}
