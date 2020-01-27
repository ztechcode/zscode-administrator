package org.zafritech.zscode.administrator.core.api.auth;

import org.zafritech.zscode.administrator.core.api.auth.models.PwdChangeRequest;
import org.zafritech.zscode.administrator.core.api.auth.models.AuthUser;
import org.zafritech.zscode.administrator.core.api.auth.models.LoginRequest;
import org.zafritech.zscode.administrator.core.api.auth.models.LoginResponse;
import org.zafritech.zscode.administrator.core.api.tasks.models.User;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AuthApiService {

    // Register new user
    @FormUrlEncoded
    @POST("auth/register")
    Single<User> register(@Field("device_id") String deviceId);

    @Headers("Content-Type: application/json")
    @POST("auth/login")
    Call<LoginResponse> doLogin (@Body LoginRequest request);

    @PUT("auth/password/change")
    Call<AuthUser> changeUserPassword (@Body PwdChangeRequest user);

    @GET("auth/user")
    Call<AuthUser> getUser ();

    @Headers("Content-Type: application/json")
    @GET("auth/users")
    Call<List<AuthUser>> fetchUsers (@Header("Authorization")  String token);

    @PUT("auth/update")
    Call<AuthUser> updateUser (@Body AuthUser user);

}
