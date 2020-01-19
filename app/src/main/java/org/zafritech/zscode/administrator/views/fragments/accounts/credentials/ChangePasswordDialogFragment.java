package org.zafritech.zscode.administrator.views.fragments.accounts.credentials;

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

public class ChangePasswordDialogFragment extends AppCompatDialogFragment {

    private TextInputEditText currentPassword;
    private TextInputEditText newPassword;
    private TextInputEditText confirmPassword;
    private Button cancelPasswordChange;
    private Button saveNewPassword;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_accounts_dialog_password, null);

        builder.setView(view).setTitle("Change Password");

        currentPassword = view.findViewById(R.id.password_current_text_input);
        newPassword = view.findViewById(R.id.password_new_text_input);
        confirmPassword = view.findViewById(R.id.password_confirm_text_input);

        cancelPasswordChange = view.findViewById(R.id.accounts_password_cancel_button);
        saveNewPassword = view.findViewById(R.id.accounts_password_save_button);

        cancelPasswordChange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        saveNewPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String password = currentPassword.getText().toString();
                String newpassword = newPassword.getText().toString();
                String confirmation = confirmPassword.getText().toString();

                Intent intent = AccountCredentialsFragment.newPasswordChangeData(password, newpassword, confirmation);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                dismiss();
            }
        });

        return builder.create();
    }

}
