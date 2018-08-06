package gui.market;

//todo: getters

public class MarketOrder {

    private String exchange;
    private String instrument;
    private int amt;
    private int slip;
    private double firstPrice;
    private double lastPrice;
    private boolean showPrices;

    MarketOrder(String exchange, String instrument, int amt, int slip, double firstPrice, double lastPrice, boolean showPrices) {

        this.exchange = exchange;
        this.instrument = instrument;
        this.amt = amt;
        this.slip = slip;
        this.firstPrice = firstPrice;
        this.lastPrice = lastPrice;
        this.showPrices = showPrices;
    }

    public String getExchange() { return exchange; }
    public String getInstrument() { return instrument; }
    public int getAmt() { return amt; }
    public int getSlip() { return slip; }
    public double getFirstPrice() { return firstPrice; }
    public double getLastPrice() { return lastPrice; }

    public boolean isShowPrices() { return showPrices; }
}
