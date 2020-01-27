package org.zafritech.zscode.administrator.core.api.accounts;

import org.zafritech.zscode.administrator.core.api.accounts.models.Account;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccountsApiService {

    // Register new user
    @FormUrlEncoded
    @POST("accounts/apikey")
    Single<String> getApiKey(@Field("device_id") String deviceId);

    // Fetch single accounts
    @GET("accounts/id/{id}")
    Single<Account> fetchAccount(@Path("id") Long id);

    // Fetch all accounts
    @GET("accounts/list")
    Single<List<Account>> fetchAllAccounts();
}
