package gui.market;

public class MarketOrder {

    public String exchange;
    public String instrument;
    public int amt;
    public int slip;

    public MarketOrder(String exchange, String instrument, int amt, int slip) {
        this.exchange = exchange;
        this.instrument = instrument;
        this.amt = amt;
        this.slip = slip;
    }

}
