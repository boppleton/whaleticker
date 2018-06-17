package websocket.exchange.bitmex.dto.liq;

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
public class BitmexLiqBase {

    @JsonProperty("table")
    private String table;
    @JsonProperty("action")
    private String action;
    @JsonProperty("data")
    private List<BitmexLiqData> data = null;

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

    @JsonProperty("data")
    public List<BitmexLiqData> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<BitmexLiqData> data) {
        this.data = data;
    }

}