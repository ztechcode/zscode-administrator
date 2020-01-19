package org.zafritech.zscode.administrator.core.api.auth;

import org.zafritech.zscode.administrator.core.api.auth.models.PwdChangeRequest;
import org.zafritech.zscode.administrator.core.api.auth.models.AuthUser;
import org.zafritech.zscode.administrator.core.api.auth.models.LoginRequest;
import org.zafritech.zscode.administrator.core.api.auth.models.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AuthApiService {


    @Headers("Content-Type: application/json")
    @POST("login")
    Call<LoginResponse> doLogin (@Body LoginRequest request);

    @PUT("password/change")
    Call<AuthUser> changeUserPassword (@Body PwdChangeRequest user);

    @GET("user")
    Call<AuthUser> getUser ();

    @Headers("Content-Type: application/json")
    @GET("users")
    Call<List<AuthUser>> fetchUsers (@Header("Authorization")  String token);

    @PUT("update")
    Call<AuthUser> updateUser (@Body AuthUser user);

}
