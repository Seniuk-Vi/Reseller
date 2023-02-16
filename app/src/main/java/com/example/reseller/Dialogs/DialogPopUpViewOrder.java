package com.example.reseller.Dialogs;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.reseller.Book;
import com.example.reseller.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DialogPopUpViewOrder extends DialogFragment {
    private String Title, Desc, Image, UserID, ItemID;
    private int Price;
    private EditText CustomeName, CustomeLocation, CustomerPhone;
    private Button Clear, Confirm;
    private Book book;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setWindowAnimations(R.style.DialogAnimation);
        View rootview = inflater.inflate(R.layout.dialog_order, container);
        CustomeName = rootview.findViewById(R.id.name);
        CustomeLocation = rootview.findViewById(R.id.location);
        CustomerPhone = rootview.findViewById(R.id.phone);
        Confirm = rootview.findViewById(R.id.confirm_order);
        Clear = rootview.findViewById(R.id.clearOrder);
        try {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle args = getArguments();
        Title = args.getString("title");
        Desc = args.getString("desc");
        Image = args.getString("image");
        UserID = args.getString("userID");
        Price = Integer.valueOf(args.getString("price"));
        ItemID = args.getString("itemID");
        book = new Book();
        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomeName.setText("");
                CustomeLocation.setText("");
                CustomerPhone.setText("");
            }
        });
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
        return rootview;
    }

    public void onClick(View v) {


    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);


    }

    private void uploadFile() {

        if (!checkLocation() | !checkName() | !checkPhone()) {
            return;
        }

        Toast.makeText(getActivity(), "Order pushed", Toast.LENGTH_LONG).show();

//                    final Task<Uri> downnloadUrl = imageRef.getDownloadUrl();
//                    downnloadUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String customerName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String cutomerID = FirebaseAuth.getInstance().getUid();
        // Створення відділу для окремого юзера

        final DatabaseReference myRef = database.getReference().child(UserID + "Orders");
        final DatabaseReference myRef2 = database.getReference().child(UserID + "Purchses");
        DatabaseReference specimenReference = myRef.push();
        book.setCustomerName(CustomeName.getText().toString().trim());
        book.setCustomerLocation(CustomeLocation.getText().toString().trim());
        book.setCustomerPhone(CustomerPhone.getText().toString().trim());
        book.setImageUrl(Image);
        book.setUserID(UserID);
        book.setCustomerID(cutomerID);
        book.setPrice(Price);
        book.setName(Title);
        book.setStatus("TODO");
        book.setNew(true);
        book.setDescription(Desc);
        // збереження DTO, та отримання ключа
        String key = specimenReference.getKey();
        book.setThumbnail(key);
        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference().child("test");
        final DatabaseReference mCounterRef = mRootRef.child(ItemID);
        // listen for single change
        mCounterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // getValue() returns Long
                long count = (long) dataSnapshot.child("itemOrders").getValue();

                mCounterRef.child("itemOrders").setValue(++count);  // <= Change to ++count

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
        specimenReference.setValue(book);
//        myRef.push().setValue(book);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 3000);
//                            userbook = book;
//                            userRef.push().setValue(userbook);

//                        }
//                    });
    }

//            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                    mProgressBar.setProgress((int) progress);
//
//                }
//            });



    private boolean checkName() {
        String headingInput = CustomeName.getText().toString().trim();
        if (headingInput.length() < 5) {
            CustomeName.setError("Minimum 5 characters");
            return false;
        } else {
            CustomeName.setError(null);
            return true;
        }
    }

    private boolean checkPhone() {
        String descInput = CustomerPhone.getText().toString().trim();
        if (descInput.length() != 10) {
            CustomerPhone.setError("10 characters required");
            return false;
        } else {
            CustomerPhone.setError(null);
            return true;
        }
    }

    private boolean checkLocation() {
        String priceInput = CustomeLocation.getText().toString().trim();


        if (priceInput.isEmpty()) {
            CustomeLocation.setError("Required field");
            return false;
        } else {
            CustomeLocation.setError(null);
            return true;
        }

    }
}