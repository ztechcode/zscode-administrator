package org.zafritech.zscode.administrator.views.fragments.services.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import org.zafritech.zscode.administrator.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ServiceEditDialog extends AppCompatDialogFragment {

    private TextInputEditText serviceName;
    private TextInputEditText serviceUrl;
    private Button cancelServiceCreation;
    private Button saveCreatedService;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_service_dialog_create, null);

        builder.setView(view).setTitle("New Service");

        serviceName = view.findViewById(R.id.service_name_text_input);
        serviceUrl = view.findViewById(R.id.service_url_text_input);
        cancelServiceCreation = view.findViewById(R.id.service_create_cancel_button);
        saveCreatedService = view.findViewById(R.id.service_create_save_button);

        cancelServiceCreation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                dismiss();
            }
        });

        saveCreatedService.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String name = serviceName.getText().toString();
                String url = serviceUrl.getText().toString();

                Intent intent = ServicesFragment.addNewServiceData(name, url);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                dismiss();
            }
        });

        return builder.create();
    }
}
