package gui.liq;

//todo: getters

public class LiqOrder {

    private String exchange;
    private String instrument;
    private int amt;
    private int slip;
    private double firstPrice;
    private double lastPrice;

    LiqOrder(String exchange, String instrument, int amt, int slip, double firstPrice, double lastPrice) {

        this.exchange = exchange;
        this.instrument = instrument;
        this.amt = amt;
        this.slip = slip;
        this.firstPrice = firstPrice;
        this.lastPrice = lastPrice;
    }

    public String getExchange() { return exchange; }
    public String getInstrument() { return instrument; }
    public int getAmt() { return amt; }
    public int getSlip() { return slip; }
    public double getFirstPrice() { return firstPrice; }
    public double getLastPrice() { return lastPrice; }

}
