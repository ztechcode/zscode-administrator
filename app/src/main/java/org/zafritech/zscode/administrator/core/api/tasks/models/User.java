package org.zafritech.zscode.administrator.core.api.tasks.models;

import com.google.gson.annotations.SerializedName;

import org.zafritech.zscode.administrator.core.api.BaseResponse;

public class User extends BaseResponse {

    @SerializedName("token_key")
    String tokenKey;

    public String getTokenKey() {

        return tokenKey;
    }
}
