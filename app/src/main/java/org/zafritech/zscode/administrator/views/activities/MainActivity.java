package org.zafritech.zscode.administrator.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.core.api.auth.AuthHelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        AuthHelper auth = new AuthHelper(context);

        // Not logged in
        if (!auth.validAuthToken()) {

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration =  new AppBarConfiguration.Builder(R.id.nav_services,
                                                                R.id.nav_service_details,
                                                                R.id.nav_accounts,
                                                                R.id.nav_account_credentials,
                                                                R.id.nav_account_contacts,
                                                                R.id.nav_resources,
                                                                R.id.nav_tasks,
                                                                R.id.nav_tasks_today,
                                                                R.id.nav_messages,
                                                                R.id.nav_workflow,
                                                                R.id.nav_notes,
                                                                R.id.nav_share,
                                                                R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        View view = findViewById(android.R.id.content);

        switch (item.getItemId()) {

            case R.id.action_settings:

                navController.navigate(R.id.nav_settings);
                return true;

            case R.id.action_logout:

                logout();
                return true;

            default:
                return false;

        }
    }

    public void logout() {

        AuthHelper auth = new AuthHelper(context);

        if (auth.logout()) {

            Intent intent = new Intent(context, StartupActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
