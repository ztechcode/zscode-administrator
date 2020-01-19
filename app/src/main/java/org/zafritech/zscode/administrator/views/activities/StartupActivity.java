package org.zafritech.zscode.administrator.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.core.auth.services.Authentication;

public class StartupActivity extends AppCompatActivity {

    private Context context;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        context = getApplicationContext();

        Authentication auth = new Authentication(context);
        handler = new Handler();

        // To be moved to settings actions
        auth.saveAuthenticationItem(auth.KEY_SECRET, "JWTZafritechSESuperSecretKey");
        auth.saveAuthenticationItem(auth.KEY_ISSUER, "Zafritech Auth Service");

        // Fake login - replace with real authentication
        if (auth.validAuthToken()) {

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }, 2000);

        } else {

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }, 2000);

        }
    }
}
