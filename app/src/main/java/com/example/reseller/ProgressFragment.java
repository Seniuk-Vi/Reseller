package com.example.reseller;

import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexzh.circleimageview.CircleImageView;
import com.bumptech.glide.Glide;
import com.example.reseller.Adapters.MyStorageRecyclerViewAdapter;
import com.example.reseller.Adapters.UserRecyclerViewAdapter;
import com.example.reseller.Dialogs.DialogEmail;
import com.example.reseller.Dialogs.DialogName;
import com.example.reseller.Dialogs.DialogPassword;
import com.example.reseller.Dialogs.DialogPopUpView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;

public class ProgressFragment extends Fragment {
    private CardView Name, Password, Email, Photo, Delete, LogOut;
    private CircleImageView Image;
    private TextView name, email;
    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private DialogFragment dlg1, dlg2, dlg3, dlg4, dlg5, dlg6;
    public byte[] finalImage;
    public Bitmap compressImage = null;
    private Uri imageUri;
    private Uri resultUri;
    private File url;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_progress, container, false);
        Image = rootView.findViewById(R.id.user_photo);
        name = rootView.findViewById(R.id.user_name);
        email = rootView.findViewById(R.id.user_email);
        fUser = fAuth.getInstance().getCurrentUser();
        Name = rootView.findViewById(R.id.profileName);
        Password = rootView.findViewById(R.id.profilePassword);
        Email = rootView.findViewById(R.id.profileEmail);
        Delete = rootView.findViewById(R.id.profileDelete);
        LogOut = rootView.findViewById(R.id.profileLogout);
        Photo = rootView.findViewById(R.id.profilePhoto);
        dlg1 = new DialogName();
        dlg2 = new DialogPassword();
        dlg3 = new DialogEmail();
        dlg4 = new DialogPassword();
        Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dlg1.setTargetFragment(ProgressFragment.this, 1);
                dlg1.show(getFragmentManager(), "dlg1");

            }
        });
        Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg2.setTargetFragment(ProgressFragment.this, 1);
                dlg2.show(getFragmentManager(), "dlg2");
            }
        });
        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg3.setTargetFragment(ProgressFragment.this, 1);
                dlg3.show(getFragmentManager(), "dlg3");
            }
        });
        Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(getContext(), ProgressFragment.this);

            }
        });
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getContext(), SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Delete");
                dialog.setMessage("Are you shure?");
                dialog.setCancelable(true);

// Specifying a listener allows you to take an action before dismissing the dialog.
// The dialog is automatically dismissed when a dialog button is clicked.
                dialog.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.delete();
                            }
                        });

// A null listener allows the button to dismiss the dialog and take no further action.

                dialog.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                AlertDialog alert = dialog.create();
                alert.show();
            }
        });
        if (fUser != null) {
            Glide.with(this).load(fUser.getPhotoUrl()).into(Image);
            name.setText(fUser.getDisplayName());
            email.setText(fUser.getEmail());
        } else {

        }

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == PICK_IMAGE_REQUEST &&
//                data != null && data.getData() != null) {
//            mImageUri = data.getData();
//            Picasso.with(getActivity())
//                    .load(mImageUri)
//                    .into(mImageVIew);
//            CropImage.activity()
//                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setAspectRatio(1, 1)
//                    .start(getActivity());
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = CropImage.getPickImageResultUri(getContext(), data);

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
//                    .setRequestedSize(640, 480)
//                    .setAspectRatio(1, 1)
                    .start(getContext(), ProgressFragment.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == -1) {
                resultUri = result.getUri();
                url = new File(result.getUri().getPath());
                try {
                    compressImage = new Compressor(getContext())
                            .setMaxHeight(300)
                            .setMaxWidth(600)
                            .setQuality(75)
                            .compressToBitmap(url);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                compressImage.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                finalImage = baos.toByteArray();
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(resultUri)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
//                                    Toast.makeText(getContext(), "Photo changed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }

    }
}
