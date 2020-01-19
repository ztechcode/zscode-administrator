package org.zafritech.zscode.administrator.core.api.models.notes;

import com.google.gson.annotations.SerializedName;

import org.zafritech.zscode.administrator.core.api.models.BaseResponse;

public class User extends BaseResponse {

    @SerializedName("token_key")
    String tokenKey;

    public String getTokenKey() {

        return tokenKey;
    }
}
