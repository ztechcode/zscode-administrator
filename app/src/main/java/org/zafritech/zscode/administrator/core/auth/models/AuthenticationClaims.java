package org.zafritech.zscode.administrator.core.auth.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "sub",
        "eml",
        "fnm",
        "lnm",
        "authorities",
        "iat",
        "exp",
        "iss"
})
public class AuthenticationClaims {

    @JsonProperty("sub")
    private String sub;

    @JsonProperty("eml")
    private String eml;

    @JsonProperty("fnm")
    private String fnm;

    @JsonProperty("lnm")
    private String lnm;

    @JsonProperty("authorities")
    private List<String> authorities = null;

    @JsonProperty("iat")
    private String iat;

    @JsonProperty("exp")
    private String exp;

    @JsonProperty("iss")
    private String iss;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
}
