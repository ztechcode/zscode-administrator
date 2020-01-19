package org.zafritech.zscode.administrator.core.auth.services;

import org.zafritech.zscode.administrator.core.auth.models.AuthenticationPasswordChangeRequest;
import org.zafritech.zscode.administrator.core.auth.models.AuthenticationUser;
import org.zafritech.zscode.administrator.core.auth.models.LoginRequest;
import org.zafritech.zscode.administrator.core.auth.models.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AuthenticationService {

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<LoginResponse> doLogin (@Body LoginRequest request);

    @PUT("password/change")
    Call<AuthenticationUser> changeUserPassword (@Body AuthenticationPasswordChangeRequest user);

    @GET("user")
    Call<AuthenticationUser> getUser ();

    @Headers("Content-Type: application/json")
    @GET("users")
    Call<List<AuthenticationUser>> fetchUsers (@Header("Authorization")  String token);

    @PUT("update")
    Call<AuthenticationUser> updateUser (@Body AuthenticationUser user);
}
