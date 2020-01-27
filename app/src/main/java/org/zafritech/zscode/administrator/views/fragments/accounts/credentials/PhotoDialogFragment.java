package org.zafritech.zscode.administrator.views.fragments.accounts.credentials;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.zafritech.zscode.administrator.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class PhotoDialogFragment extends AppCompatDialogFragment {

    private static final String PHOTO_SOURCE_CAMERA = "PHOTO_SOURCE_CAMERA";
    private static final String PHOTO_SOURCE_GALLERY = "PHOTO_SOURCE_GALLERY";

    private LinearLayout takePhotoLayout;
    private LinearLayout chooseFromGalleryLayout;
    private Button cancelPasswordChange;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_accounts_dialog_photo, null);

        builder.setView(view).setTitle("Change Profile Picture");

        takePhotoLayout = view.findViewById(R.id.accounts_photo_from_camera_layout);
        chooseFromGalleryLayout = view.findViewById(R.id.accounts_photo_from_gallery_layout);
        cancelPasswordChange = view.findViewById(R.id.accounts_password_cancel_button);

        takePhotoLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = CredentialsFragment.newProfilePhotoSourceData(PHOTO_SOURCE_CAMERA);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                dismiss();
            }
        });

        chooseFromGalleryLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = CredentialsFragment.newProfilePhotoSourceData(PHOTO_SOURCE_GALLERY);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                dismiss();
            }
        });

        cancelPasswordChange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

        return builder.create();
    }
}
