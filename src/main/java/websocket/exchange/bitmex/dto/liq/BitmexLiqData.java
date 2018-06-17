package websocket.exchange.bitmex.dto.liq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "orderID",
        "symbol",
        "side",
        "price",
        "leavesQty"
})
public class BitmexLiqData {

    @JsonProperty("orderID")
    private String orderID;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("side")
    private String side;
    @JsonProperty("price")
    private Integer price;
    @JsonProperty("leavesQty")
    private Integer leavesQty;

    @JsonProperty("orderID")
    public String getOrderID() {
        return orderID;
    }

    @JsonProperty("orderID")
    public void setOrderID(String orderID) {
        this.orderID = orderID;
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

    @JsonProperty("price")
    public Integer getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(Integer price) {
        this.price = price;
    }

    @JsonProperty("leavesQty")
    public Integer getLeavesQty() {
        return leavesQty;
    }

    @JsonProperty("leavesQty")
    public void setLeavesQty(Integer leavesQty) {
        this.leavesQty = leavesQty;
    }

}