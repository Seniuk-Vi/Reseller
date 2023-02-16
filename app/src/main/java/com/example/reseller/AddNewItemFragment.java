package com.example.reseller;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;

public class AddNewItemFragment extends Fragment {
    private Button btnPushItems;
    private EditText Heading, Description, Price;
    private Book book;
    //    Book userbook;
    private static final int PICK_IMAGE_REQUEST = 5;
    private ImageView mButtonChooseImage;
    private Button mButtonUpload;
    private ProgressBar mProgressBar;
    private ImageView mImageVIew;
    private Uri mImageUri;
    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    private TextInputLayout textinputHeading;
    private TextInputLayout textInputDesc;
    private TextInputLayout textInputPrice;
    private TextInputLayout textInputCat;
    private ArrayList<Uri> mSelected;
    private SwitchMaterial mSwitch; // switch
    private boolean privacyBool;
    private String UserName, mFirebaseUser;
    private String UserPhoto;
    //    private DatabaseReference userRef;
    // compress photo
    public byte[] finalImage;
    public Bitmap compressImage = null;
    private Uri imageUri;
    private Uri resultUri;
    private File url;
    private EditText heading;
    private Spinner spinner;
    private TextView advertise;
    private String adv;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_addnewitem, container, false);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference().child("test");
        //push image\
        mButtonUpload = rootview.findViewById(R.id.upload);
        mProgressBar = rootview.findViewById(R.id.progress_barr);
        mImageVIew = rootview.findViewById(R.id.add_image_photo);
        mButtonChooseImage = rootview.findViewById(R.id.add_image_photo);
        advertise = rootview.findViewById(R.id.advertise);
        mSwitch = rootview.findViewById(R.id.switch_privasy);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser().getUid();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        advertise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Advertise?");
                dialog.setMessage("Google Pay in development");
                dialog.setCancelable(true);

// Specifying a listener allows you to take an action before dismissing the dialog.
// The dialog is automatically dismissed when a dialog button is clicked.
                dialog.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                adv = "yes";
                                dialog.cancel();
                            }
                        });

// A null listener allows the button to dismiss the dialog and take no further action.

                dialog.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                adv = "yes";
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = dialog.create();
                alert.show();
            }
        });
//        userRef = database.getReference().child(mFirebaseUser);
        UserName = mFirebaseAuth.getCurrentUser().getDisplayName();
        UserPhoto = String.valueOf(mFirebaseAuth.getCurrentUser().getPhotoUrl());


        //Spinner
        spinner = rootview.findViewById(R.id.spinner_category_add);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.Categories, R.layout.spinner_text);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        mStorageReference = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(getContext(), AddNewItemFragment.this);
            }
        });
        textinputHeading = rootview.findViewById(R.id.text_input_Heading_ERROR);
        textInputCat = rootview.findViewById(R.id.text_input_cat_ERROR);
        textInputDesc = rootview.findViewById(R.id.text_input_Description_ERROR);
        textInputPrice = rootview.findViewById(R.id.text_input_Price_ERROR);

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mImageVIew.getDrawable() != null) {
                    newuploadFile();
                } else {
                    Toast.makeText(getActivity(), "Choose photo!", Toast.LENGTH_LONG).show();
                }
            }
        });


        //push into firebase database
//        btnPushItems = rootview.findViewById(R.id.addItemButton);

        Description = rootview.findViewById(R.id.text_input_Description);
        Heading = rootview.findViewById(R.id.text_input_Heading);
        Price = rootview.findViewById(R.id.text_input_Price);
        book = new Book();
//        userbook = new Book();

        return rootview;
    }

    private void openFileChooser() {
//        Matisse.from(MainActivity.this)
//                .choose(MimeType.allOf())
//                .countable(true)
//                .maxSelectable(9)
//                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
//                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                .thumbnailScale(0.85f)
//                .imageEngine(new GlideEngine())
//                .forResult(REQUEST_CODE_CHOOSE);
        Intent intent = new Intent();
        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
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
                    .start(getContext(), AddNewItemFragment.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == -1) {
                resultUri = result.getUri();
                url = new File(result.getUri().getPath());
                Picasso.with(getContext())
                        .load(url)
                        .into(mImageVIew);

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
            }
        }

    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void newuploadFile() {
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("images");
        final StorageReference imageRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        if (!checkDesc() | !checkHeading() | !checkPrice() | !checkCat()) {
            return;
        }

        if (resultUri != null) {
            UploadTask uploadTask = imageRef.putBytes(finalImage);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    int i = 1 + 1;
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(0);
                        }
                    }, 500);
                    Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();
                    StorageMetadata storageMetadata = taskSnapshot.getMetadata();
                    final Task<Uri> downnloadUrl = imageRef.getDownloadUrl();
                    downnloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();


                            // Створення відділу для окремого юзера

                            final DatabaseReference myRef = database.getReference().child("test");
                            String imageReference = uri.toString();
                            DatabaseReference specimenReference = myRef.push();
                            book.setUserPhoto(UserPhoto);
                            book.setUserName(UserName);
                            book.setImageUrl(imageReference);
                            book.setUserID(mFirebaseUser);
                            book.setPrice(Integer.parseInt(Price.getText().toString().trim()));
                            book.setName(Heading.getText().toString().trim());
                            book.setDescription(Description.getText().toString().trim());
                            book.setCategory(spinner.getSelectedItem().toString());
                            book.setItemOrders(0);
                            book.setItemViews(0);
                            book.setComplains(0);
                            book.setStatus(" ");
                            book.setAdv("Yes");
                            if (adv.equals("yes")){
                                book.setAdv("yes");
                            }else book.setAdv("no");
                            if (mSwitch.isChecked()) {
                                privacyBool = true;
                            } else privacyBool = false;

                            book.setPrivacy(privacyBool);
                            // збереження DTO, та отримання ключа
                            String key = specimenReference.getKey();
                            book.setThumbnail(key);
                            specimenReference.setValue(book);
//                            userbook = book;
//                            userRef.push().setValue(userbook);

                        }
                    });
                }
            });
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mProgressBar.setProgress((int) progress);

                }
            });
        }

    }

//    private boolean checkAddMenu() {
//
//
//        if(checkDesc()|checkHeading()|checkPrice()){
//
//        return true;
//        }
//
//    }

    private boolean checkHeading() {
        String headingInput = textinputHeading.getEditText().getText().toString().trim();
        if (headingInput.length() < 5) {
            textinputHeading.setError("Minimum 5 characters");
            return false;
        } else {
            textinputHeading.setError(null);
            return true;
        }
    }

    private boolean checkDesc() {
        String descInput = textInputDesc.getEditText().getText().toString().trim();
        if (descInput.length() < 30) {
            textInputDesc.setError("Minimum 30 characters");
            return false;
        } else {
            textInputDesc.setError(null);
            return true;
        }
    }

    private boolean checkPrice() {
        String priceInput = textInputPrice.getEditText().getText().toString().trim();


        if (priceInput.isEmpty()) {
            textInputPrice.setError("Required field");
            return false;
        } else {
            textInputPrice.setError(null);
            return true;
        }
    }

    private boolean checkCat() {
        String priceInput = spinner.getSelectedItem().toString();

        if (priceInput.equals("Category")) {
            textInputCat.setError("Choose category");
            return false;
        } else {
            textInputCat.setError(null);
            return true;
        }
    }


}

