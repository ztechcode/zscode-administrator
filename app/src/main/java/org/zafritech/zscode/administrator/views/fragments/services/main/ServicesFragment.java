package org.zafritech.zscode.administrator.views.fragments.services.main;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.data.db.services.Service;
import org.zafritech.zscode.administrator.data.db.services.ServiceRepository;
import org.zafritech.zscode.administrator.data.db.services.ServicesInit;
import org.zafritech.zscode.administrator.views.fragments.services.common.ServiceItem;
import org.zafritech.zscode.administrator.views.fragments.services.common.ServicesClickInterface;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ServicesFragment extends Fragment {

    private static final Integer SERVICE_CREATE_REQUEST_CODE = 0;
    private static final String EXTRA_SERVICE_NAME = "serviceName";
    private static final String EXTRA_SERVICE_URL = "serviceUrl";
    private static final String SERVICE_CREATE_TAG = "PHOTO_CHANGE_DIALOG_TAG";

    private ServicesFragment context;
    private RecyclerView recyclerView;
    private ServiceItem serviceItem;
    private ArrayList<ServiceItem> serviceArrayList;
    private ServicesRecyclerViewAdapter adapter;
    private ServicesClickInterface onClickInterface;

    public static ServicesFragment newInstance()
    {
        return new ServicesFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        final Bundle bundle = new Bundle();

        serviceArrayList = new ArrayList<>();

        View root = inflater.inflate(R.layout.fragment_services, null);
        FloatingActionButton fab = root.findViewById(R.id.fab);
        recyclerView = root.findViewById(R.id.service_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fab.setOnClickListener(view -> {

            ServiceCreateDialogFragment dialogFragment = new ServiceCreateDialogFragment();
            dialogFragment.setTargetFragment(ServicesFragment.this, SERVICE_CREATE_REQUEST_CODE);
            dialogFragment.show(getFragmentManager(), SERVICE_CREATE_TAG);
        });

        onClickInterface = (view, position) -> {

            serviceItem = serviceArrayList.get(position);
            bundle.putString("name", serviceItem.getName());

            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_service_details, bundle);
        };

        // Perform Async Task to initialise database if empty
        // And load RecyclerView data
        new LoadServicesTask().execute();

        return root;
    }

    public void setAdapter() {

        adapter = new ServicesRecyclerViewAdapter(getActivity(), serviceArrayList, onClickInterface);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_services, menu);
    }

    @Override
    public String toString() {

        return "ZTS Services";
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_refresh_server:

                new LoadServicesTask().execute();
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode != Activity.RESULT_OK ) { return; }

        if( requestCode == SERVICE_CREATE_REQUEST_CODE ) {

            String name = data.getStringExtra(EXTRA_SERVICE_NAME);
            String url = data.getStringExtra(EXTRA_SERVICE_URL);

            // Perform Async Task - save (db insert) item
            Service service = new Service(name, url);
            new InsertServiceTask().execute(service);

            Snackbar.make(getView(), "Server data: " + name + " | " + url, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public static Intent addNewServiceData(String name, String url) {

        Intent intent = new Intent();
        intent.putExtra(EXTRA_SERVICE_NAME, name);
        intent.putExtra(EXTRA_SERVICE_URL, url);

        return intent;
    }

    private class InsertServiceTask extends AsyncTask<Service, Void, Service> {

        @Override
        protected Service doInBackground(Service... services) {

            ServiceRepository repository = new ServiceRepository(getActivity().getApplication());
            Service service = repository.insert(services[0]);

            return service;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(Service service) {

            serviceArrayList.add(0, createService(service.getName(), service.getUrl()));
            adapter.notifyItemInserted(0);

        }
    }

    private class LoadServicesTask extends AsyncTask<Void, Void,  List<Service>> {

        @Override
        protected List<Service> doInBackground(Void... voids) {

            ServiceRepository repository = new ServiceRepository(getActivity().getApplication());
            List<Service> services = repository.getAllServicesList();

            if (services.isEmpty()) {

                ServicesInit init = new ServicesInit(getActivity().getApplication());
                init.init();
                SystemClock.sleep(3000); // Give time to generate data
                return repository.getAllServicesList();
            }

            return repository.getAllServicesList();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(List<Service> services) {

            serviceArrayList.clear();

            for (Service service : services) {

                ServiceItem item = new ServiceItem();
                item.setImgIcon(null);
                item.setName(service.getName());
                item.setUrl(service.getUrl());
                item.setStatus((ThreadLocalRandom.current().nextInt(0, 50) < 40) ? "RUNNING" : "STOPPED");
                item.setCpu("CPU: " + (new Random().nextInt(80) + 10) + "%");
                item.setUptime(randomFormattedDate());
                serviceArrayList.add(item);
            }

            setAdapter();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ServiceItem createService(String name, String url) {

        ServiceItem item = new ServiceItem();
        item.setImgIcon(null);
        item.setName(name);
        item.setUrl(url);
        item.setStatus((ThreadLocalRandom.current().nextInt(0, 50) < 40) ? "RUNNING" : "STOPPED");
        item.setCpu("CPU: " + (new Random().nextInt(80) + 10) + "%");
        item.setUptime(randomFormattedDate());

        return item;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String randomFormattedDate() {

        LocalDateTime from = LocalDateTime.of(2019,10,18,8,0,0,0);
        ZonedDateTime timeFrom = from.atZone(ZoneId.of("Europe/London"));
        long timeFromMillis = timeFrom.toInstant().toEpochMilli();

        LocalDateTime to = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        ZonedDateTime timeTo = to.atZone(ZoneId.of("Europe/London"));
        long timeToMillis = timeTo.toInstant().toEpochMilli();

        long random = ThreadLocalRandom.current().nextLong(timeFromMillis, timeToMillis);

        long secondsDuration = (timeToMillis - random) / 1000 ;  // Total minutes
        long days = secondsDuration / 86400;
        long hours = (secondsDuration % 86400) / 3660;
        long minutes = (secondsDuration % 3660) / 60;

        String duration = days + "d " + hours + "h " + minutes + "m";

        return duration;
    }
}