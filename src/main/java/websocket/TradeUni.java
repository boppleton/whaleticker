package websocket;

public class TradeUni {

    private String exchangeName;
    private String instrument;
    private double size;
    private boolean side;
    private double price;
    private String timestamp;
    private String id;
    private double firstPrice;
    private double lastPrice;

    public TradeUni() { }

    public TradeUni(String exchangeName, String instrument, double size, boolean side, double price, String timestamp, String id) {
        this.exchangeName = exchangeName;
        this.instrument = instrument;
        this.size = size;
        this.side = side;
        this.price = price;
        this.timestamp = timestamp;
        this.id = id;
    }

    public String getExchangeName() { return exchangeName; }
    public void setExchangeName(String exchangeName) { this.exchangeName = exchangeName; }

    public String getInstrument() {
        return instrument;
    }
    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public double getSize() {
        return size;
    }
    public void setSize(double size) {
        this.size = size;
    }

    public boolean getSide() {
        return side;
    }
    public void setSide(boolean side) {
        this.side = side;
    }

    public double getPrice() {
        String price = String.format("%.1f", this.price);
        return Double.parseDouble(price);
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public double getFirstPrice() { return firstPrice; }
    public void setFirstPrice(double firstPrice) { this.firstPrice = firstPrice; }

    public double getLastPrice() { return lastPrice; }
    public void setLastPrice(double lastPrice) { this.lastPrice = lastPrice; }

}
