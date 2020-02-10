package org.zafritech.zscode.administrator.views.fragments.tasks;


import android.app.Activity;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.zafritech.zscode.administrator.R;
import org.zafritech.zscode.administrator.core.api.ApiClient;
import org.zafritech.zscode.administrator.core.api.tasks.TasksApiService;
import org.zafritech.zscode.administrator.core.api.tasks.models.Task;
import org.zafritech.zscode.administrator.data.db.tasks.Word;
import org.zafritech.zscode.administrator.data.db.tasks.WordViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class TaskEditFragment extends Fragment {

    public static final String EXTRA_REPLY = "org.zafritech.zscode.tasks.wordlistsql.REPLY";
    private static final Integer DATE_SET_REQUEST_CODE = 0;
    private static final Integer TIME_SET_REQUEST_CODE = 1;
    private static final String DATE_SET_TAG = "DATE_SET_DIALOG_TAG";
    private static final String TIME_SET_TAG = "TIME_SET_DIALOG_TAG";
    private static final String EXTRA_DATE_YEAR = "dateYear";
    private static final String EXTRA_DATE_MONTH = "dateMonth";
    private static final String EXTRA_DATE_DAY = "dateDay";
    private static final String EXTRA_TIME_HOUR = "timeHour";
    private static final String EXTRA_TIME_MINUTE = "timeMinute";
    private static final String EXTRA_TIME_24HRS = "time24hrs";

    private TasksApiService apiService;
    private WordViewModel mWordViewModel;
    TextInputEditText calenderInput;
    TextInputEditText taskDetailsInput;
    TextInputEditText timeInput;
    private EditText mEditWordView;
    private TextView repeatOptionsTextView;
    private RadioGroup repeatRadioGoup;
    private RadioButton repeatNone;
    private RadioButton repeatInterval;
    private RadioButton repeatScheduled;

    public static TaskEditFragment newInstance()
    {

        return new TaskEditFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);

        View view = inflater.inflate(R.layout.fragment_task_edit, container, false);
        mEditWordView = view.findViewById(R.id.tasks_edit_task_text_view);
        taskDetailsInput = view.findViewById(R.id.tasks_edit_task_text_view);
        calenderInput = view.findViewById(R.id.task_date_text_input_edit_text);
        timeInput = view.findViewById(R.id.task_time_text_input_edit_text);
        repeatRadioGoup = view.findViewById(R.id.task_repeat_options_radio);
        repeatNone = view.findViewById(R.id.task_repeat_none);
        repeatInterval = view.findViewById(R.id.task_repeat_interval);
        repeatScheduled = view.findViewById(R.id.task_repeat_scheduled);
        repeatOptionsTextView = view.findViewById(R.id.task_repeat_options_detail_text);

        calenderInput.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.setTargetFragment(TaskEditFragment.this, DATE_SET_REQUEST_CODE);
                datePicker.setCancelable(false);
                datePicker.show(getFragmentManager(), DATE_SET_TAG);
            }
        });

        timeInput.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                TimePickerFragment timePicker = new TimePickerFragment();
                timePicker.setTargetFragment(TaskEditFragment.this, TIME_SET_REQUEST_CODE);
                timePicker.setCancelable(false);
                timePicker.show(getFragmentManager(), TIME_SET_TAG);
            }
        });

        repeatRadioGoup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {

                    case R.id.task_repeat_none:

                        repeatOptionsTextView.setVisibility(View.GONE);

                        break;
                    case R.id.task_repeat_interval:

                        repeatOptionsTextView.setText("Repeat every 28 days");
                        repeatOptionsTextView.setVisibility(View.VISIBLE);

                        break;
                    case R.id.task_repeat_scheduled:

                        repeatOptionsTextView.setText("Repeat every Mon, Wed, Fri");
                        repeatOptionsTextView.setVisibility(View.VISIBLE);
                        // TODO: To be implemented on server first

                        break;
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        apiService = ApiClient.getClient(getActivity().getApplicationContext()).create(TasksApiService.class);

        Long id = getArguments().getLong("id");
        fetchTaskForEdit(id);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.menu_item_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save_item:

                if (!TextUtils.isEmpty(mEditWordView.getText())) {

                    Word word = new Word(mEditWordView.getText().toString());

                    mWordViewModel.insert(word);

                    // Hide keypad after editing
                    mEditWordView.setFocusable(false);
                    mEditWordView.setFocusableInTouchMode(true);
                }

                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_tasks);

                return true;

            case R.id.action_delete_item:

                Snackbar.make(getView(), "You want to delete task!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                return true;

            default:
                return false;
        }
    }

    private void fetchTaskForEdit(Long id){

        apiService.getTask(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Task>() {

                    @Override
                    public void onSuccess(Task task) {

                        loadTaskForEdit(task);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    private void loadTaskForEdit(Task task) {

        SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        taskDetailsInput.setText(task.getDetails());

        if (task.getDeadline() != null) {

            try {

                String date = task.getDeadline().replace("T", " ");
                Date deadline = (task.getDeadline() != null) ? inputFormatter.parse(date) : new Date();
                calenderInput.setText(dateFormatter.format(deadline));
                timeInput.setText(timeFormatter.format(deadline));

            } catch (ParseException e) {

                e.printStackTrace();
            }
        }

        if (task.getRepeat() != null) {

            //TODO: Add Repeat Options display here

        } else {

            repeatOptionsTextView.setVisibility(View.GONE);
        }
    }

    public static Intent deadLineDateSet(Integer year, Integer month, Integer day) {

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE_YEAR, year);
        intent.putExtra(EXTRA_DATE_MONTH, month);
        intent.putExtra(EXTRA_DATE_DAY, day);

        return intent;
    }

    public static Intent deadLineTimeSet(Integer hour, Integer minute, boolean is24hrs) {

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME_HOUR, hour);
        intent.putExtra(EXTRA_TIME_MINUTE, minute);
        intent.putExtra(EXTRA_TIME_24HRS, is24hrs);

        return intent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode != Activity.RESULT_OK ) { return; }

        if( requestCode == DATE_SET_REQUEST_CODE ) {

            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            Integer year = data.getIntExtra(EXTRA_DATE_YEAR, calendar.get(Calendar.YEAR));
            Integer month = data.getIntExtra(EXTRA_DATE_MONTH, calendar.get(Calendar.MONTH));
            Integer day = data.getIntExtra(EXTRA_DATE_DAY, calendar.get(Calendar.DAY_OF_MONTH));

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            calenderInput.setText(dateFormat.format(calendar.getTime()));
        }

        if( requestCode == TIME_SET_REQUEST_CODE ) {

            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            Integer hour = data.getIntExtra(EXTRA_TIME_HOUR, calendar.get(Calendar.HOUR_OF_DAY));
            Integer minute = data.getIntExtra(EXTRA_TIME_MINUTE, calendar.get(Calendar.MINUTE));

            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);

            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            timeInput.setText(dateFormat.format(calendar.getTime()));
        }
    }
}
