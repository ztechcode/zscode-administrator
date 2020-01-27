package org.zafritech.zscode.administrator.views.fragments.tasks;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.core.api.tasks.models.Task;
import org.zafritech.zscode.administrator.core.utils.DividerItemDecoration;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TodayFragment extends Fragment {

    private RecyclerView taskRecyclerView;
    private ArrayList<Task> taskList = new ArrayList<>();
    private TaskListAdapter mAdapter;

    public static TodayFragment newInstance() {

        return new TodayFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_tasks_today, null);
        FloatingActionButton fab = view.findViewById(R.id.fab);

        taskRecyclerView = view.findViewById(R.id.task_list_today_recycler_view);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskRecyclerView.setAdapter(mAdapter);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_tasks_edit);
            }
        });

        fetchTasks();

        mAdapter = new TaskListAdapter(getContext(), taskList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        taskRecyclerView.setLayoutManager(mLayoutManager);
        taskRecyclerView.setItemAnimator(new DefaultItemAnimator());
        taskRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        taskRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void fetchTasks() {

        for (int i = 1; i < 11; i++) {

            Task task = new Task("Today's task number " + i);
            task.setCategory("Family");
            task.setDue(new Date(System.currentTimeMillis()).toString());

            taskList.add(task);
        }
    }
}
