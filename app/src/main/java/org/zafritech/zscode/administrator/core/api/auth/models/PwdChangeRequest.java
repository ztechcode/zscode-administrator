package org.zafritech.zscode.administrator.core.api.auth.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "password",
    "newPassword",
    "newPasswordRepeat"
})
public class PwdChangeRequest {

    @JsonProperty("oldPassword")
    public String oldPassword;

    @JsonProperty("newPassword")
    public String newPassword;

    @JsonProperty("newConfirmation")
    public String newConfirmation;

    public PwdChangeRequest(String oldPassword, String newPassword, String newConfirmation) {

        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.newConfirmation = newConfirmation;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getNewConfirmation() {
        return newConfirmation;
    }
}
