package org.zafritech.zscode.administrator.core.auth.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "uuid",
    "email",
    "name"
})
public class AuthenticationUser {

    @JsonProperty("id")
    public Integer id;

    @JsonProperty("uuid")
    public String uuid;

    @JsonProperty("email")
    public String email;

    @JsonProperty("name")
    public Object name;
}
