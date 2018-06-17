package websocket.exchange.bitmex.dto.trade;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "table",
        "action",
        "data"
})
public class BitmexTrades {

    @JsonProperty("table")
    private String table;
    @JsonProperty("action")
    private String action;
    @JsonProperty("info")
    private String info;
    @JsonProperty("version")
    private String version;
    @JsonProperty("keys")
    private String keys;



    @JsonProperty("success")
    private String success;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("request")
    private String request;
    @JsonProperty("docs")
    private String docs;

    @JsonProperty("subscribe")
    private String subscribe;
    @JsonProperty("unsubscribe")
    private String unsubscribe;

    @JsonProperty("data")
    private List<BitmexTrade> data = null;

    @JsonProperty("table")
    public String getTable() {
        return table;
    }
    @JsonProperty("table")
    public void setTable(String table) {
        this.table = table;
    }

    @JsonProperty("action")
    public String getAction() {
        return action;
    }
    @JsonProperty("action")
    public void setAction(String action) {
        this.action = action;
    }

    @JsonProperty("info")
    public String getInfo() {
        return info;
    }
    @JsonProperty("info")
    public void setInfo(String keys) {
        this.info = info;
    }


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    @JsonProperty("data")
    public List<BitmexTrade> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<BitmexTrade> data) {
        this.data = data;
    }

}