package org.zafritech.zscode.administrator.core.api.auth.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "username",
    "password"
})
public class LoginRequest {

    @JsonProperty("username")
    public String username;

    @JsonProperty("password")
    public String password;

    public LoginRequest(String username, String password) {

        this.username = username;
        this.password = password;
    }
}
