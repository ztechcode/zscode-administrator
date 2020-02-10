package org.zafritech.zscode.administrator.views.fragments.tasks;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.core.api.ApiClient;
import org.zafritech.zscode.administrator.core.api.tasks.TasksApiService;
import org.zafritech.zscode.administrator.core.api.tasks.models.BasicTask;
import org.zafritech.zscode.administrator.core.api.tasks.models.Category;
import org.zafritech.zscode.administrator.core.api.tasks.models.Note;
import org.zafritech.zscode.administrator.core.api.tasks.models.Schedule;
import org.zafritech.zscode.administrator.core.api.tasks.models.Task;
import org.zafritech.zscode.administrator.core.api.tasks.models.TasksRequestDate;
import org.zafritech.zscode.administrator.core.utils.DividerItemDecoration;
import org.zafritech.zscode.administrator.core.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class TasksFragment extends Fragment {

    private static final String EXTRA_TASK_ID = "taskId";

    private CompositeDisposable disposable = new CompositeDisposable();
    private static final String TAG = TasksFragment.class.getSimpleName();
    private TasksFragment context;
    private LinearLayout inboxLayout;
    private LinearLayout todayLayout;
    private LinearLayout upcomingLayout;
    private LinearLayout somedayLayout;
    private LinearLayout logbookLayout;
    private RecyclerView categoryRecyclerView;
    private ProgressBar dataLoading;
    private TasksApiService apiService;
    private ArrayList<Category> categoryList = new ArrayList<>();
    private CategoryListAdapter mAdapter;

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    public static TasksFragment newInstance() {

        return new TasksFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_tasks_home_page, null);
        FloatingActionButton fab = view.findViewById(R.id.fab);

        dataLoading = view.findViewById(R.id.task_empty_progress_bar);
        categoryRecyclerView = view.findViewById(R.id.tasks_categories_recycler_view);
        inboxLayout = view.findViewById(R.id.tasks_inbox_horizontal_layout);
        todayLayout = view.findViewById(R.id.tasks_today_horizontal_layout);
        upcomingLayout = view.findViewById(R.id.tasks_upcoming_horizontal_layout);
        somedayLayout = view.findViewById(R.id.tasks_anytime_horizontal_layout);
        logbookLayout = view.findViewById(R.id.tasks_logbook_horizontal_layout);

        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoryRecyclerView.setAdapter(mAdapter);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                newTaskDialogShow();
            }
        });

        inboxLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Inbox Tasks Selected!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        todayLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_tasks_today);
            }
        });

        upcomingLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Upcoming Tasks Selected!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        somedayLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Someday Tasks Selected!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        logbookLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Snackbar.make(view, "Task Logbook Selected!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        categoryRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),categoryRecyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {

                Snackbar.make(view, "Selected category at position: " + position, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

            @Override
            public void onLongClick(View view, int position) {

                Snackbar.make(view, "Long clicked category at position: " + position, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        }));

        apiService = ApiClient.getClient(getActivity().getApplicationContext()).create(TasksApiService.class);

        fetchCategories();

        mAdapter = new CategoryListAdapter(getContext(), categoryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        categoryRecyclerView.setLayoutManager(mLayoutManager);
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        categoryRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_tasks, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        switch (item.getItemId()) {

            case R.id.action_add_task_item:

                navController.navigate(R.id.nav_tasks_edit);
                return true;

            default:
                return false;
        }
    }

    private void newTaskDialogShow() {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View view = layoutInflaterAndroid.inflate(R.layout.fragment_task_new, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(view);

        final TextInputEditText newTaskText = view.findViewById(R.id.tasks_new_task_text_view);
        newTaskText.setPadding(8, 8, 8, 8);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText("New Task");

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorGreenDark));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorRed));
            }
        });

        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Show toast message when no text is entered
                if (TextUtils.isEmpty(newTaskText.getText().toString())) {

                    Toast.makeText(getActivity(), "Enter tesk details!", Toast.LENGTH_LONG).show();
                    return;

                } else {

                    alertDialog.dismiss();
                }

                createTask(newTaskText.getText().toString().trim());
            }
        });
    }

    private void createTask(String details) {

        disposable.add(apiService.createTask(new BasicTask(details))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Task>() {

                    @Override
                    public void onSuccess(Task task) {

                        editTask(task);
                    }

                    @Override
                    public void onError(Throwable e) {

                        Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }));

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
                        mAdapter.notifyDataSetChanged();

                        dataLoading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void fetchTasksByDate(TasksRequestDate dateFilter) {

        apiService.fetchTasksByDate(dateFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Schedule>>() {

                    @Override
                    public void onSuccess(List<Schedule> schedules) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                });
    }

    private void editTask(Task task) {

        Bundle bundle = new Bundle();
        bundle.putLong("id", task.getId());

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.nav_tasks_edit, bundle);
    }

}