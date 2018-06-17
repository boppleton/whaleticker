package websocket.exchange.bitfinex.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigInteger;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class TradeUpdate {

    public Integer startNum;
    public String tu;

    public String seq;

    public BigInteger tradeId;
    public BigInteger timestamp;
    public Double price;
    public Double amount;



}
