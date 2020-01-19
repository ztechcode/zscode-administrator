package org.zafritech.zscode.administrator.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.core.api.auth.AuthApiService;
import org.zafritech.zscode.administrator.core.api.auth.models.LoginRequest;
import org.zafritech.zscode.administrator.core.api.auth.models.LoginResponse;
import org.zafritech.zscode.administrator.core.api.auth.AuthHelper;
import org.zafritech.zscode.administrator.core.encrypt.Crypto;
import org.zafritech.zscode.administrator.core.encrypt.CryptoService;
import org.zafritech.zscode.administrator.core.utils.Constants;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private AuthHelper auth;
    private String accessToken;
    private Context context;
    private TextInputEditText usernameText;
    private TextInputEditText passwordText;
    private TextView errorTextView;
    private Button loginButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();
        this.auth = new AuthHelper(context);

        // Already logged in - get out of here!
        if (auth.validAuthToken()) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        usernameText = (TextInputEditText) findViewById(R.id.username);
        passwordText = (TextInputEditText) findViewById(R.id.password);
        errorTextView = (TextView) findViewById(R.id.loginErrorTextView);
        loginButton = (Button) findViewById(R.id.loginButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        errorTextView.setVisibility(View.INVISIBLE);
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                LoginRequest loginRequest = new LoginRequest(usernameText.getText().toString(), passwordText.getText().toString());

                try {

                    authenticate(loginRequest);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        });

        // Already logged in?
        if (auth.validAuthToken()) {

            openMainActivity();

        } else {

            loadStoredCredentials();
        }

    }

    private void loadStoredCredentials() {

        CryptoService crypto = new Crypto(context);
        AuthHelper auth = new AuthHelper(context);

        String username = auth.fetchAuthenticationString(auth.KEY_USERNAME);
        String password = auth.fetchAuthenticationString(auth.KEY_PASSWORD);

        usernameText.setText(username);
        passwordText.setText(password);

    }

    private void clearForm() {

        usernameText.setText("");
        passwordText.setText("");
    }

    private void showLoginError(String message) {

        errorTextView.setText(message);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void authenticate(final LoginRequest loginRequest) throws Exception {

        String username = loginRequest.username;
        String password = loginRequest.password;

        System.out.println(username);
        System.out.println(password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        AuthApiService authService = retrofit.create(AuthApiService.class);

        Call<LoginResponse> call = authService.doLogin(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {

                    accessToken = response.body().token.accessToken;

                    if (response.code() == 200) {

                        // Good credentials- save them
                        auth.storeTokenKey(accessToken);
                        auth.storeUserName(username);
                        auth.storePassword(password);

                        openMainActivity();

                    } else {

                        // Bad credentials - clear all stored credentials
                        auth.removeTokenKey();
                        auth.removeUserName();
                        auth.removePassword();

                        clearForm();

                        showLoginError("Incorrect credentials. Please try again.");

                    }

                } else {

                    clearForm();

                    showLoginError("Incorrect credentials. Please try again.");

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                System.out.println(t.getMessage());
            }
        });
    }

    private void openMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
