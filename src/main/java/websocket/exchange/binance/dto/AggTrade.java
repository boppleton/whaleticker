package websocket.exchange.binance.dto;


import com.fasterxml.jackson.annotation.*;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "e",
        "E",
        "s",
        "a",
        "p",
        "q",
        "f",
        "l",
        "T",
        "m",
        "M"
})
public class AggTrade {

    @JsonProperty("e")
    private String e;
    @JsonProperty("E")
    private BigInteger eventTime;
    @JsonProperty("s")
    private String symbol;
    @JsonProperty("a")
    private BigInteger aggId;
    @JsonProperty("p")
    private String price;
    @JsonProperty("q")
    private String quantity;
    @JsonProperty("f")
    private BigInteger firstId;
    @JsonProperty("l")
    private BigInteger lastId;
    @JsonProperty("T")
    private BigInteger tradeTime;
    @JsonProperty("m")
    private Boolean buyMaker;
    @JsonProperty("M")
    private Boolean MM;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("e")
    public String getE() {
        return e;
    }

    @JsonProperty("e")
    public void setE(String e) {
        this.e = e;
    }

    @JsonProperty("E")
    public BigInteger getEventTime() {
        return eventTime;
    }

    @JsonProperty("EE")
    public void setEventTime(BigInteger eventTime) {
        this.eventTime = eventTime;
    }

    @JsonProperty("s")
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("s")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("a")
    public BigInteger getAggId() {
        return aggId;
    }

    @JsonProperty("a")
    public void setAggId(BigInteger aggId) {
        this.aggId = aggId;
    }

    @JsonProperty("p")
    public String getPrice() {
        return price;
    }

    @JsonProperty("p")
    public void setPrice(String price) {
        this.price = price;
    }

    @JsonProperty("q")
    public String getQuantity() {
        return quantity;
    }

    @JsonProperty("q")
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("f")
    public BigInteger getFirstId() {
        return firstId;
    }

    @JsonProperty("f")
    public void setFirstId(BigInteger firstId) {
        this.firstId = firstId;
    }

    @JsonProperty("l")
    public BigInteger getLastId() {
        return lastId;
    }

    @JsonProperty("l")
    public void setLastId(BigInteger lastId) {
        this.lastId = lastId;
    }

    @JsonProperty("T")
    public BigInteger getTradeTime() {
        return tradeTime;
    }

    @JsonProperty("T")
    public void setTradeTime(BigInteger tradeTime) {
        this.tradeTime = tradeTime;
    }

    @JsonProperty("m")
    public boolean getSide() { return !buyMaker; }

    @JsonProperty("m")
    public void setSide(Boolean buyMaker) {
        this.buyMaker = buyMaker;
    }

    @JsonProperty("MM")
    public Boolean getMM() {
        return MM;
    }

    @JsonProperty("MM")
    public void setMM(Boolean MM) {
        this.MM = MM;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}