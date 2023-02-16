package com.example.reseller;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.reseller.Dialogs.DialogPopUpViewPick;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderingFragment extends Fragment implements DialogPopUpViewPick.OnInputListener {
    private LinearLayout layoutPicker;
    DialogFragment dlg1;
    private TextView itemName;
    private EditText pickerName, pickerPhone, pickerAddress;
    private TextInputLayout nameError, phoneError, adressError;
    private ImageView imagePicker;
    private Book book;
    private String photo, name, desc, category, itemID;
    private int price;
    private Button order;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_ordering, container, false);
        layoutPicker = rootview.findViewById(R.id.layout_picker);
        dlg1 = new DialogPopUpViewPick();
        book = new Book();
        layoutPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dlg1.setTargetFragment(OrderingFragment.this, 1);
                dlg1.show(getFragmentManager(), "dlg1");
            }
        });
        imagePicker = rootview.findViewById(R.id.pick_item);
        itemName = rootview.findViewById(R.id.pick_item_name);
        pickerPhone = rootview.findViewById(R.id.ordering_phone);
        pickerName = rootview.findViewById(R.id.ordering_name);
        pickerAddress = rootview.findViewById(R.id.ordering_address);
        adressError = rootview.findViewById(R.id.ordering_address_error);
        nameError = rootview.findViewById(R.id.ordering_name_error);
        phoneError = rootview.findViewById(R.id.ordering_phone_error);
        order = rootview.findViewById(R.id.order_btn);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePicker.getDrawable() != null) {
                    uploadFile();
                } else {
                    Toast.makeText(getActivity(), "Pick item!", Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootview;
    }

    private void uploadFile() {

        if (!checkLocation() | !checkName() | !checkPhone()) {
            return;
        }
        if (photo.isEmpty()){
            Toast.makeText(getContext(),"Pick item!", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference myRef = database.getReference().child(UserID + "Orders");

        DatabaseReference specimenReference = myRef.push();
        book.setCustomerName(pickerName.getText().toString().trim());
        book.setCustomerLocation(pickerAddress.getText().toString().trim());
        book.setCustomerPhone(pickerPhone.getText().toString().trim());
        book.setImageUrl(photo);
        book.setUserID(UserID);
        book.setCustomerID(" ");
        book.setPrice(price);
        book.setName(name);
        book.setNew(true);
        book.setDescription(desc);
        book.setCategory(category);
        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference().child("test");
        final DatabaseReference mCounterRef = mRootRef.child(itemID);
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
        // збереження DTO, та отримання ключа
        String key = specimenReference.getKey();
        book.setThumbnail(key);
        book.setStatus("TODO");
        specimenReference.setValue(book);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                photo = "";
                name = "";
                desc= "";
                category = "";
                price = 0;
                pickerName.setText("");
                pickerAddress.setText("");
                pickerPhone.setText("");
                Glide.with(getContext()).load(R.drawable.plus).into(imagePicker);
                itemName.setText("Pick item");
            }
        }, 3000);

    }


    private boolean checkName() {
        String headingInput = pickerName.getText().toString().trim();
        if (headingInput.length() < 5) {
            nameError.setError("Minimum 5 characters");
            return false;
        } else {
            nameError.setError(null);
            return true;
        }
    }

    private boolean checkPhone() {
        String descInput = pickerPhone.getText().toString().trim();
        if (descInput.length() != 10) {
            phoneError.setError("10 characters required");
            return false;
        } else {
            phoneError.setError(null);
            return true;
        }
    }

    private boolean checkLocation() {
        String priceInput = pickerAddress.getText().toString().trim();


        if (priceInput.isEmpty()) {
            adressError.setError("Required field");
            return false;
        } else {
            adressError.setError(null);
            return true;
        }

    }
    @Override
    public void sendInput(String photo, String name, String category, String description, int price, String userId, String itemID) {
        itemName.setText(name);
        Glide.with(getActivity()).load(photo).into(imagePicker);
        this.photo = photo;
        this.name = name;
        this.category = category;
        this.price = price;
        this.desc = description;
        this.itemID = itemID;
    }
}
