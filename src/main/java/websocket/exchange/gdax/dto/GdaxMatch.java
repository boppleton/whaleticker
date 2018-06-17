package websocket.exchange.gdax.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"type",
"trade_id",
"maker_order_id",
"taker_order_id",
"side",
"size",
"price",
"product_id",
"sequence",
"time"
})
public class GdaxMatch {

    @JsonProperty("type")
    private String type;
    @JsonProperty("trade_id")
    private Integer tradeId;
    @JsonProperty("maker_order_id")
    private String makerOrderId;
    @JsonProperty("taker_order_id")
    private String takerOrderId;
    @JsonProperty("side")
    private String side;
    @JsonProperty("size")
    private String size;
    @JsonProperty("price")
    private String price;
    @JsonProperty("product_id")
    private String productId;
    @JsonProperty("sequence")
    private BigInteger sequence;
    @JsonProperty("time")
    private String time;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("trade_id")
    public Integer getTradeId() {
        return tradeId;
    }

    @JsonProperty("trade_id")
    public void setTradeId(Integer tradeId) {
        this.tradeId = tradeId;
    }

    @JsonProperty("maker_order_id")
    public String getMakerOrderId() {
        return makerOrderId;
    }

    @JsonProperty("maker_order_id")
    public void setMakerOrderId(String makerOrderId) {
        this.makerOrderId = makerOrderId;
    }

    @JsonProperty("taker_order_id")
    public String getTakerOrderId() {
        return takerOrderId;
    }

    @JsonProperty("taker_order_id")
    public void setTakerOrderId(String takerOrderId) {
        this.takerOrderId = takerOrderId;
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
    public double getSize() {

        double size = Double.parseDouble(this.size);
        double price = Double.parseDouble(this.price);

        return size*price;


    }

    @JsonProperty("size")
    public void setSize(String size) {
        this.size = size;
    }

    @JsonProperty("price")
    public String getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(String price) {
        this.price = price;
    }

    @JsonProperty("product_id")
    public String getProductId() {
        return productId;
    }

    @JsonProperty("product_id")
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @JsonProperty("sequence")
    public BigInteger getSequence() {
        return sequence;
    }

    @JsonProperty("sequence")
    public void setSequence(BigInteger sequence) {
        this.sequence = sequence;
    }

    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(String time) {
        this.time = time;
    }
}