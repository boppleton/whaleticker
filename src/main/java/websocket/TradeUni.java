package websocket;


import java.text.DecimalFormat;

public class TradeUni {

    private String exchangeName;
    private String instrument;
    private double size;
    private boolean side;
    private double price;
    private String timestamp;
    private String id;
    private boolean update = false;
    private String slip;

    private double firstPrice;
    private double lastPrice;

    private String sizeFormatted;

    public TradeUni() {

    }


    public TradeUni(String exchangeName, String instrument, double size, boolean side, double price, String timestamp, String id) {
        this.exchangeName = exchangeName;
        this.instrument = instrument;
        this.size = size;
        this.side = side;
        this.price = price;
        this.timestamp = timestamp;
        this.id = id;

    }



    public String getIcon() {

        String icon = "";

        switch (exchangeName) {
            case "bitmex":
                icon = "https://i.imgur.com/3LQBglR.png";
                break;
            case "bitfinex":
                icon = "https://i.imgur.com/7CNGpKm.png";
                break;
            case "okex":
                icon = "https://i.imgur.com/9jX2ikO.png";
                break;
            case "gdax":
                icon = "https://i.imgur.com/nbRCbWo.png";
                break;
            case "binance":
                icon = "https://i.imgur.com/03SBQwq.png";
                break;
            default:
                icon = "";
                break;
        }

        return icon;
    }

    public String getSlipIcon() {

        double slipDub;
        String slip = "";
        String up = "https://i.imgur.com/n38F1Tw.png";
        String down = "https://i.imgur.com/uTbeZbt.png";

        slipDub = lastPrice-firstPrice;

        if (slipDub >= 1) {
            slip = up;
            return slip;
        } else if (slipDub <= -1) {
            slip = down;
            return slip;
        }

        return slip;
    }

    public String getPriceIfSlip() {

        if (!getSlipIcon().equals("") && !exchangeName.contains("okex")) {

            return String.valueOf(getLastPrice());

        } else {

            return "";
        }
    }

    public String getSideLongOrShort() {

        if (side) {
            return "short";
        } else {
            return "long";
        }

    }

    public String getSlip() {

        double slipDub = lastPrice-firstPrice;

        String slip = "";

        if (slipDub >= 1) {
            slip = String.format("%.0f", slipDub);
            return slip;
        } else if (slipDub <= -1) {
            slip = String.format("%.0f", -slipDub);
            return slip;
        } else {
            return slip;
        }
    }

    // exchangName
    public String getExchangeName() { return exchangeName; }
    public void setExchangeName(String exchangeName) { this.exchangeName = exchangeName; }

    // instrument
    public String getInstrument() {
        return instrument;
    }

    public String getInstrumentClean() {
        if (instrument.contains("bitmexPerp")) {
            return "";
        } else if (instrument.contains("bitmexJune")) {
            return "june futures";
        } else if (instrument.contains("bitmexSept")) {
            return "september futures";
        } else if (instrument.contains("okexThis")) {
            return "this week futures";
        } else if (instrument.contains("okexNext")) {
            return "next week futures";
        } else if (instrument.contains("okexQuat")) {
            return "quarterly futures";
        } else {
            return "";
        }
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    // size
    public double getSize() {
        return size;
    }




    public void setSize(double size) {
        this.size = size;
    }

    // side
    public boolean getSide() {
        return side;
    }
    public void setSide(boolean side) {
        this.side = side;
    }

    // price
    public double getPrice() {
        String price = String.format("%.1f", this.price);
        return Double.parseDouble(price);
    }
    public void setPrice(double price) {
        this.price = price;
    }

    // timestamp
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // id
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // update
    public boolean isUpdate() { return update; }
    public void setUpdate(boolean update) { this.update = update; }

    public String getSizeFormatted() { return sizeFormatted; }
    public void setSizeFormatted(String sizeFormatted) { this.sizeFormatted = sizeFormatted; }

    public double getFirstPrice() { return firstPrice; }
    public void setFirstPrice(double firstPrice) { this.firstPrice = firstPrice; }

    public double getLastPrice() { return lastPrice; }
    public void setLastPrice(double lastPrice) { this.lastPrice = lastPrice; }



}
