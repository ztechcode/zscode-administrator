package org.zafritech.zscode.administrator.views.fragments.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.core.api.ApiClient;
import org.zafritech.zscode.administrator.core.api.tasks.TasksApiService;
import org.zafritech.zscode.administrator.core.api.tasks.models.Category;
import org.zafritech.zscode.administrator.core.api.tasks.models.Schedule;
import org.zafritech.zscode.administrator.core.api.tasks.models.Task;
import org.zafritech.zscode.administrator.core.api.tasks.models.TasksRequestDate;
import org.zafritech.zscode.administrator.core.api.tasks.models.TasksRequestRange;
import org.zafritech.zscode.administrator.core.utils.DividerItemDecoration;
import org.zafritech.zscode.administrator.core.utils.RecyclerTouchListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class TodayFragment extends Fragment {

    private static final Integer CATEGORY_SELECTION_REQUEST_CODE = 0;
    private static final String EXTRA_CURRENT_CATEGORY = "currentCategory";

    private CompositeDisposable disposable = new CompositeDisposable();
    private static final String TAG = TodayFragment.class.getSimpleName();
    private FragmentActivity context;
    private RecyclerView taskRecyclerView;
    private TaskScheduleAdapter mAdapter;
    private TextView categoryText;
    private TextView tasksdateText;

    private List<String> categoriesList = new ArrayList<>();
    private ArrayList<Schedule> scheduleList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    private Schedule schedule;
    private TasksApiService apiService;
    private String [] categoryChars;

    public static TodayFragment newInstance() {

        return new TodayFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_tasks_today, null);
        FloatingActionButton fab = view.findViewById(R.id.fab);

        categoryText = view.findViewById(R.id.task_filter_text_view);
        tasksdateText = view.findViewById(R.id.tasks_today_date_text_view);
        taskRecyclerView = view.findViewById(R.id.task_list_today_recycler_view);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskRecyclerView.setAdapter(mAdapter);

        fab.setOnClickListener(view1 -> {

            Bundle bundle = new Bundle();
            bundle.putLong("id", 0);

            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_tasks_edit, bundle);
        });

        taskRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), taskRecyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                schedule = scheduleList.get(position);
                Task task = schedule.getTask();
                editTask(task);
            }

            @Override
            public void onLongClick(View view, int position) {

                Snackbar.make(view, "Long clicked Task at position: " + position, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }));

        apiService = ApiClient.getClient(getActivity().getApplicationContext()).create(TasksApiService.class);

        fetchTasks("All");

        mAdapter = new TaskScheduleAdapter(getContext(), scheduleList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        taskRecyclerView.setLayoutManager(mLayoutManager);
        taskRecyclerView.setItemAnimator(new DefaultItemAnimator());
        taskRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        taskRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.menu_tasks_today, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_tasks_category_filter_item:

                fetchCategories();
                return true;

            default:
                return false;
        }
    }

    private void editTask(Task task) {

        Bundle bundle = new Bundle();
        bundle.putLong("id", task.getId());

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_tasks_edit, bundle);
    }

    public static Intent categorySelected(String category) {

        Intent intent = new Intent();
        intent.putExtra(EXTRA_CURRENT_CATEGORY, category);

        return intent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode != Activity.RESULT_OK ) { return; }

        if( requestCode == CATEGORY_SELECTION_REQUEST_CODE ) {

            String current = data.getStringExtra(EXTRA_CURRENT_CATEGORY);
            categoryText.setText(current.toUpperCase());

            fetchTasks(current);
        }

    }

    private void fetchTasks(String filter) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 90);

        Date today = new Date();
        Date seven = calendar.getTime();
        String isoToday = isoDateTimeFormatter(new Date());
        String isoSeven = isoDateTimeFormatter(calendar.getTime());

        TasksRequestDate dateFilter = new TasksRequestDate(isoSeven, filter);
//        TasksRequestRange rangeFilter = new TasksRequestRange(isoToday, isoSeven, filter);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
//        tasksdateText.setText(dateFormat.format(today));
        tasksdateText.setText(dateFormat.format(today) + " - " + dateFormat.format(seven));

        fetchTasksByDate(dateFilter);
//        fetchTasksByDateRange(rangeFilter);
    }

    private void fetchCategories() {

        apiService.fetchCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Category>>() {

                    @Override
                    public void onSuccess(List<Category> categories) {

                        System.out.println("Success! Account count: " + categories.size());

                        categoryList.clear();
                        categoryList.addAll(categories);

                        categoryChars = new String [categoryList.size()];

                        for (int i = 0; i < categoryList.size(); i++) {

                            categoryChars[i] = categoryList.get(i).getName();
                        }

                        DialogCategories dialog = new DialogCategories(categoryChars);
                        dialog.setTargetFragment(TodayFragment.this, CATEGORY_SELECTION_REQUEST_CODE);
                        dialog.show(getFragmentManager(), TAG);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void fetchTasksByDate(TasksRequestDate dateFilter) {

        apiService.fetchTasksUpToDate(dateFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Schedule>>() {

                    @Override
                    public void onSuccess(List<Schedule> schedules) {

                        scheduleList.clear();
                        scheduleList.addAll(schedules);
                        mAdapter.notifyDataSetChanged();

                        System.out.println("Schedules: " + schedules);
                    }

                    @Override
                    public void onError(Throwable e) {

                        System.out.println("Failure: " + e.getMessage());
                    }

                });
    }

    private void fetchTasksByDateRange(TasksRequestRange rangeFilter) {

        apiService.fetchAllTasksByDateRange(rangeFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Schedule>>() {

                    @Override
                    public void onSuccess(List<Schedule> schedules) {

                        scheduleList.clear();
                        scheduleList.addAll(schedules);
                        mAdapter.notifyDataSetChanged();

                        System.out.println("Schedules: " + schedules);
                    }

                    @Override
                    public void onError(Throwable e) {

                        System.out.println("Failure: " + e.getMessage());
                    }

                });
    }

    private String isoDateTimeFormatter(Date date) {

        String pattern = "yyyy-MM-dd HH:mm:ss.SSSZZZZZ";
        SimpleDateFormat dateFormatter = new SimpleDateFormat(pattern);

        String dateString = dateFormatter.format(date).replace(" ", "T");

        return dateString;
    }
}
