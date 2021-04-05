
package rudaks.types;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "AppID",
    "CreatedTime"
})
public class HeartBeat {

    @JsonProperty("AppID")
    private String appID;
    @JsonProperty("CreatedTime")
    private String createdTime;

    @JsonProperty("AppID")
    public String getAppID() {
        return appID;
    }

    @JsonProperty("AppID")
    public void setAppID(String appID) {
        this.appID = appID;
    }

    public HeartBeat withAppID(String appID) {
        this.appID = appID;
        return this;
    }

    @JsonProperty("CreatedTime")
    public String getCreatedTime() {
        return createdTime;
    }

    @JsonProperty("CreatedTime")
    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public HeartBeat withCreatedTime(String createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("appID", appID).append("createdTime", createdTime).toString();
    }

}
