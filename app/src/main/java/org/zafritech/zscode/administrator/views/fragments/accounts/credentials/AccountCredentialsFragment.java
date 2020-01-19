package org.zafritech.zscode.administrator.views.fragments.accounts.credentials;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import org.zafritech.zscode.administrator.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class AccountCredentialsFragment extends Fragment {

    private static final Integer PASSWORD_CHANGE_REQUEST_CODE = 0;
    private static final Integer PHOTO_CHANGE_REQUEST_CODE = 1;
    private static final Integer PHOTO_CHANGE_CAMERA_REQUEST_CODE = 2;
    private static final Integer PHOTO_CHANGE_GALLERY_REQUEST_CODE = 3;
    private static final String PASSWORD_CHANGE_TAG = "PASSWORD_CHANGE_DIALOG_TAG";
    private static final String PHOTO_CHANGE_TAG = "PHOTO_CHANGE_DIALOG_TAG";
    private static final String EXTRA_CURRENT_PASSWORD = "currentPassword";
    private static final String EXTRA_NEW_PASSWORD = "newPassword";
    private static final String EXTRA_CONFIRMATION_PASSWORD = "confirmPassword";
    private static final String EXTRA_PHOTO_SOURCE = "photoSource";
    private static final String PHOTO_SOURCE_CAMERA = "PHOTO_SOURCE_CAMERA";
    private static final String PHOTO_SOURCE_GALLERY = "PHOTO_SOURCE_GALLERY";

    private FrameLayout profilePictureChange;
    private LinearLayout passwordChangeLayout;
    private Button buttonSaveCredentials;
    private Button buttonEditContacts;
    private String currentPhotoPath;
    private boolean cameraPermissionGranted;
    private boolean galleryPermissionGranted;

    public AccountCredentialsFragment newInstance()
    {
        return new AccountCredentialsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        cameraPermissionGranted = false;
        galleryPermissionGranted = false;

        View root = inflater.inflate(R.layout.fragment_accounts_credentials, container, false);

        profilePictureChange = root.findViewById(R.id.accounts_photo_change_layout);
        passwordChangeLayout = root.findViewById(R.id.accounts_password_change_layout);
        buttonSaveCredentials = root.findViewById(R.id.button_save_credentials);
        buttonEditContacts = root.findViewById(R.id.button_edit_contacts);

        profilePictureChange.setOnClickListener(view -> {

            ChangePhotoDialogFragment dialogFragment = new ChangePhotoDialogFragment();
            dialogFragment.setTargetFragment(AccountCredentialsFragment.this, PHOTO_CHANGE_REQUEST_CODE);
            dialogFragment.show(getFragmentManager(), PHOTO_CHANGE_TAG);

        });

        passwordChangeLayout.setOnClickListener(view -> {

            ChangePasswordDialogFragment dialogFragment = new ChangePasswordDialogFragment();
            dialogFragment.setTargetFragment(AccountCredentialsFragment.this, PASSWORD_CHANGE_REQUEST_CODE);
            dialogFragment.show(getFragmentManager(), PASSWORD_CHANGE_TAG);

        });

        buttonSaveCredentials.setOnClickListener(view -> {

            Snackbar.make(view, "Credentials saved.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

        buttonEditContacts.setOnClickListener(view -> {

            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
            navController.navigate(R.id.nav_account_contacts);

        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getArguments().getString("name"));
        ImageView imgView_profile = view.findViewById(R.id.details_profile_image);

        Glide.with(getContext()).load("https://source.unsplash.com/random?w=100")
                .placeholder(R.drawable.ic_profile_placeholder)
                .error(R.drawable.ic_profile_question)
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imgView_profile);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode != Activity.RESULT_OK ) { return; }

        if( requestCode == PASSWORD_CHANGE_REQUEST_CODE ) {

            String current = data.getStringExtra(EXTRA_CURRENT_PASSWORD);
            String password = data.getStringExtra(EXTRA_NEW_PASSWORD);
            String confirmation = data.getStringExtra(EXTRA_CONFIRMATION_PASSWORD);

            Snackbar.make(getView(), "Passwords: " + current + " | " + password + " | " + confirmation, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

        }

        if( requestCode == PHOTO_CHANGE_REQUEST_CODE ) {

            String photoSource = data.getStringExtra(EXTRA_PHOTO_SOURCE);

            // Get photo from Camera
            if (photoSource.equals(PHOTO_SOURCE_CAMERA)) {

                checkCameraPermission();

                if (cameraPermissionGranted) {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

                        try {

                            File photoFile = createImageFile();

                            if (photoFile != null) {

                                Uri photoURI = FileProvider.getUriForFile(getActivity(), "net.zafritech.fileprovider", photoFile);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                startActivityForResult(takePictureIntent, PHOTO_CHANGE_CAMERA_REQUEST_CODE);
                            }

                        } catch (IOException ex) {

                            ex.printStackTrace();
                        }
                    }

                } else {

                    Snackbar.make(getView(), "Camera permission denied.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }

            // Get photo from Gallery
            if (photoSource.equals(PHOTO_SOURCE_GALLERY)) {

                checkGalleryPermission();

                if (galleryPermissionGranted) {

                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(pickPhoto, PHOTO_CHANGE_GALLERY_REQUEST_CODE);

                } else {

                    Snackbar.make(getView(), "Photo Gallery permission denied.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        }

        if( requestCode == PHOTO_CHANGE_CAMERA_REQUEST_CODE ) {

            saveAndDisplayPhoto();
        }

        if( requestCode == PHOTO_CHANGE_GALLERY_REQUEST_CODE ) {

            Uri photoUri = data.getData();
            currentPhotoPath = getPathFromURI(photoUri);

            saveAndDisplayPhoto();

        }
    }

    public static Intent newPasswordChangeData(String current, String password, String confirmation) {

        Intent intent = new Intent();
        intent.putExtra(EXTRA_CURRENT_PASSWORD, current);
        intent.putExtra(EXTRA_NEW_PASSWORD, password);
        intent.putExtra(EXTRA_CONFIRMATION_PASSWORD, confirmation);

        return intent;
    }

    public static Intent newProfilePhotoSourceData(String source) {

        Intent intent = new Intent();
        intent.putExtra(EXTRA_PHOTO_SOURCE, source);

        return intent;
    }

    private void checkCameraPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            // Request permission - to be granted or denied in onRequestPermissionsResult() below
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PHOTO_CHANGE_CAMERA_REQUEST_CODE);

        } else {

            // Already granted.
            cameraPermissionGranted = true;
        }

    }

    private void checkGalleryPermission() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Request permission - to be granted or denied in onRequestPermissionsResult() below
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PHOTO_CHANGE_GALLERY_REQUEST_CODE);

        } else {

            // Already granted.
            galleryPermissionGranted = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PHOTO_CHANGE_CAMERA_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                cameraPermissionGranted = true;

            } else {

                cameraPermissionGranted = false;
                Toast.makeText(getActivity(), "Permission denied to use camera", Toast.LENGTH_SHORT).show();
            }

            return;
        }

        if (requestCode == PHOTO_CHANGE_GALLERY_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                galleryPermissionGranted = true;

            } else {

                galleryPermissionGranted = false;
                Toast.makeText(getActivity(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }

            return;
        }

    }

    private void saveAndDisplayPhoto() {

        ImageView imgView_profile = getView().findViewById(R.id.details_profile_image);

        Glide.with(getActivity())
                .load(currentPhotoPath)
                .apply(new RequestOptions().centerCrop()
                .circleCrop()
                .placeholder(R.drawable.ic_profile_placeholder))
                .into(imgView_profile);
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,  ".jpg",   storageDir);

        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    public String getPathFromURI(Uri contentUri) {

        String path = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);

        if (cursor.moveToFirst()) {

            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            path = cursor.getString(column_index);

        }

        cursor.close();

        return path;
    }
}