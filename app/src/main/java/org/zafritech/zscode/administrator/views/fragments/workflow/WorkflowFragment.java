package org.zafritech.zscode.administrator.views.fragments.workflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.zafritech.zscode.administrator.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class WorkflowFragment extends Fragment {

    private WorkflowViewModel workflowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        workflowViewModel = ViewModelProviders.of(this).get(WorkflowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_workflow, container, false);
        final TextView textView = root.findViewById(R.id.text_workflow);

        workflowViewModel.getText().observe(this, new Observer<String>() {

            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }

        });

        return root;
    }
}