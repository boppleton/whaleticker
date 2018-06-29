package gui.liq;

//todo: getters

public class LiqOrder {

    private String exchange;
    private String instrument;
    private int amt;
    private int slip;
    private double price;
    private String updates;
    private boolean side;
    private boolean active = true;

    private String timestamp;
    private String id;


    public LiqOrder(String exchange, String s, int amount, boolean side, double price, String time, String id, boolean active) {
        this.exchange = exchange;
        this.instrument = s;
        this.amt = amount;
        this.side = side;
        this.price = price;
        this.timestamp = time;
        this.id = id;
        this.active = active;
    }

    public String getExchange() { return exchange; }
    public String getInstrument() { return instrument; }
    public int getAmt() { return amt; }
    public int getSlip() { return slip; }


    public String getUpdates() {
        return updates;
    }

    public String getId() {
        return id;
    }

    public int getSize() {
        return amt;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public void setSize(int size) {
        this.amt = size;
    }

    public boolean getSide() {
        return side;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
