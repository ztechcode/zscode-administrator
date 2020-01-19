package org.zafritech.zscode.administrator.core.auth.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "tokenType",
    "accessToken",
    "expiry"
})
public class AuthenticationToken {

    @JsonProperty("tokenType")
    public String tokenType;

    @JsonProperty("accessToken")
    public String accessToken;

    @JsonProperty("expiry")
    public Long expiry;
}
