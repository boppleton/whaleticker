package gui;

public class MarketOrder {

    public String exchange;
    public int amt;
    public int slip;

    public MarketOrder(String exchange, int amt, int slip) {
        this.exchange = exchange;
        this.amt = amt;
        this.slip = slip;
    }

}
