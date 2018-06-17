package websocket.exchange.bitmex.dto.trade;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigInteger;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "timestamp",
        "symbol",
        "side",
        "size",
        "price",
        "tickDirection",
        "trdMatchID",
        "grossValue",
        "homeNotional",
        "foreignNotional"
})
public class BitmexTrade {

    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("side")
    private String side;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("price")
    private double price;
    @JsonProperty("tickDirection")
    private String tickDirection;
    @JsonProperty("trdMatchID")
    private String trdMatchID;
    @JsonProperty("grossValue")
    private BigInteger grossValue;
    @JsonProperty("homeNotional")
    private Double homeNotional;
    @JsonProperty("foreignNotional")
    private Integer foreignNotional;

    public static BitmexTrade newTrade(int size, String side) {
        BitmexTrade t = new BitmexTrade();
        t.setSide(side);
        t.setSize(size);
        return t;
    }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("symbol")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("side")
    public String getSide() {
        return side;
    }

    @JsonProperty("side")
    public void setSide(String side) {
        this.side = side;
    }

    @JsonProperty("size")
    public Integer getSize() {
        return size;
    }

    @JsonProperty("size")
    public void setSize(Integer size) {
        this.size = size;
    }

    @JsonProperty("price")
    public double getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(double price) {
        this.price = price;
    }

    @JsonProperty("tickDirection")
    public String getTickDirection() {
        return tickDirection;
    }

    @JsonProperty("tickDirection")
    public void setTickDirection(String tickDirection) {
        this.tickDirection = tickDirection;
    }

    @JsonProperty("trdMatchID")
    public String getTrdMatchID() {
        return trdMatchID;
    }

    @JsonProperty("trdMatchID")
    public void setTrdMatchID(String trdMatchID) {
        this.trdMatchID = trdMatchID;
    }

    @JsonProperty("grossValue")
    public BigInteger getGrossValue() {
        return grossValue;
    }

    @JsonProperty("grossValue")
    public void setGrossValue(BigInteger grossValue) {
        this.grossValue = grossValue;
    }

    @JsonProperty("homeNotional")
    public Double getHomeNotional() {
        return homeNotional;
    }

    @JsonProperty("homeNotional")
    public void setHomeNotional(Double homeNotional) {
        this.homeNotional = homeNotional;
    }

    @JsonProperty("foreignNotional")
    public Integer getForeignNotional() {
        return foreignNotional;
    }

    @JsonProperty("foreignNotional")
    public void setForeignNotional(Integer foreignNotional) {
        this.foreignNotional = foreignNotional;
    }

}