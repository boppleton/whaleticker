package websocket.exchange.bitmex.book;

import java.math.BigInteger;

public class BitmexBookEvent {

    private String action;
    private String side;
    private int size;
    private double price;
    private BigInteger id;

    public BitmexBookEvent(String action, String side, int size, double price, BigInteger id) {
        this.action = action;
        this.side = side;
        this.size = size;
        this.price = price;
        this.id = id;
    }

    @Override
    public String toString() {
        return id + action + " - " + side + " " + size + " @ " + price;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }
}
