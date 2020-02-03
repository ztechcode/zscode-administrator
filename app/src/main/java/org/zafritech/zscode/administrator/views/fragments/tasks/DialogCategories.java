package org.zafritech.zscode.administrator.views.fragments.tasks;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import org.zafritech.zscode.administrator.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogCategories extends DialogFragment {

    private String [] categories;

    public DialogCategories(String[] categories) {

        this.categories = categories;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        builder.setItems(categories, (dialog, which) -> {

            Intent intent = TodayFragment.categorySelected(categories[which]);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

            dismiss();
        });

        return builder.create();
    }

}
