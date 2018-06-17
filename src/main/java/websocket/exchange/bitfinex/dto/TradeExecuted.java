package websocket.exchange.bitfinex.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

//using tu for now because te is returning differing types for id

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class TradeExecuted {

    public Integer startNum;
    public String te;

    public String seq;

    public long timestamp;
    public double price;
    public double amount;

}
