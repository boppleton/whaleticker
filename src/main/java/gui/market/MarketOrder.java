package gui.market;

public class MarketOrder {

    public String exchange;
    public String instrument;
    public int amt;
    public int slip;
    public double firstPrice;
    public double lastPrice;

    public MarketOrder(String exchange, String instrument, int amt, int slip, double firstPrice, double lastPrice) {
        this.exchange = exchange;
        this.instrument = instrument;
        this.amt = amt;
        this.slip = slip;
        this.firstPrice = firstPrice;
        this.lastPrice = lastPrice;
    }

}
