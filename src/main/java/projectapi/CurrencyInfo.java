package projectapi;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CurrencyInfo {

    @JsonProperty("bid")
    private double bid;

    @JsonProperty("create_date")
    private String createDate;

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public String getCreateDate() {
        return createDate;
    }

}