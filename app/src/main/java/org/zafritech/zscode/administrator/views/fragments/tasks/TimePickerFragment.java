package org.zafritech.zscode.administrator.views.fragments.tasks;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public interface TimePickerListener {

        void onTimeSet(TimePicker timePicker, int hour, int minute, boolean is24hrs);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar cal = Calendar.getInstance();
        int mHour = cal.get(Calendar.HOUR_OF_DAY);
        int mMinute = cal.get(Calendar.MINUTE);

        return new TimePickerDialog(getContext(), this, mHour, mMinute, false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {

        Intent intent = TaskEditFragment.deadLineTimeSet(hour, minute, true);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

        dismiss();
    }

}
