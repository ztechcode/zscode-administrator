package org.zafritech.zscode.administrator.core.auth.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "user",
        "token"
})
public class LoginResponse {

    @JsonProperty("user")
    public AuthenticationUser user;

    @JsonProperty("token")
    public AuthenticationToken token;
}
