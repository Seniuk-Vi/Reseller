package com.example.reseller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.reseller.Adapters.MyStorageRecyclerViewAdapter;
import com.example.reseller.Adapters.UserRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Book_User_Activity extends AppCompatActivity {

    private EditText name, phone, location;
    private TextView title;
    private ImageView img;
    private DatabaseReference mDatabaseRef;
    private List<Book> mData;
    private TextInputLayout name_error, phone_error, location_error;
    private DatabaseReference myRef, myRef2;
    private String itemID;
    private Button confirm, delete;
    private FloatingActionButton back;
    private String user;
    private String customerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_);


        name = findViewById(R.id.book_mystorage_name);
        phone = findViewById(R.id.book_mystorage_phone);
        location = findViewById(R.id.book_mystorage_location);
        img = (ImageView) findViewById(R.id.book_mystorage_image);
        title = findViewById(R.id.txttitle);
        back = findViewById(R.id.back);
        location_error = findViewById(R.id.book_mystorage_location_error);
        name_error = findViewById(R.id.book_mystorage_name_error);
        phone_error = findViewById(R.id.book_mystorage_phone_error);
        confirm = findViewById(R.id.confirmtodo);
        delete = findViewById(R.id.deletetodo);
        Intent intent = getIntent();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String Title = intent.getExtras().getString("Title");
        String Description = intent.getExtras().getString("Description");
        String image = intent.getExtras().getString("Image");
        itemID = intent.getExtras().getString("ItemID");

        user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Присвоєння значення
        title.setText(Title);
        name.setText(intent.getExtras().getString("Name"));
        phone.setText(intent.getExtras().getString("Phone"));
        location.setText(intent.getExtras().getString("Location"));
        Glide.with(this).load(image).into(img);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem();
            }
        });
//        Picasso.with(this).load(thumbnai).placeholder(R.drawable.ic_faq).into(img);
        myRef = FirebaseDatabase.getInstance().getReference().child(user + "Orders").child(itemID).child("new");
//        myRef2 = FirebaseDatabase.getInstance().getReference().child(customerRef + "Purchases").child(itemID).child("new");
        myRef.setValue(false);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef = FirebaseDatabase.getInstance().getReference().child(user + "Orders").child(itemID);
                myRef.removeValue();
                Toast.makeText(Book_User_Activity.this, "Order deleted", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 3000);

            }
        });
    }

    private void updateItem() {

        if (!checkLocation() | !checkName() | !checkPhone()) {

            return;
        }
        //Heading
        myRef = FirebaseDatabase.getInstance().getReference().child(user + "Orders").child(itemID).child("customerName");
        myRef.setValue(name.getText().toString());
        //Description
        myRef = FirebaseDatabase.getInstance().getReference().child(user + "Orders").child(itemID).child("customerPhone");
        myRef.setValue(phone.getText().toString());
        //Price
        myRef = FirebaseDatabase.getInstance().getReference().child(user + "Orders").child(itemID).child("customerLocation");
        myRef.setValue(location.getText().toString());
        myRef = FirebaseDatabase.getInstance().getReference().child(user + "Orders").child(itemID).child("status");
        myRef.setValue("PROCESSING");

//        //purshases
//        //Heading
//        myRef = FirebaseDatabase.getInstance().getReference().child(customerRef + "Purchases").child(itemID).child("customerName");
//        myRef.setValue(name.getText().toString());
//        //Description
//        myRef = FirebaseDatabase.getInstance().getReference().child(customerRef + "Purchases").child(itemID).child("customerPhone");
//        myRef.setValue(phone.getText().toString());
//        //Price
//        myRef = FirebaseDatabase.getInstance().getReference().child(customerRef + "Purchases").child(itemID).child("customerLocation");
//        myRef.setValue(location.getText().toString());
//        myRef = FirebaseDatabase.getInstance().getReference().child(customerRef + "Purchases").child(itemID).child("status");
//        myRef.setValue("PROCESSING");
        Toast.makeText(getApplicationContext(), "Confirmed", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
//        ((UserRecyclerViewAdapter) FragmentForOrdersSec.mRecyclerView.getAdapter()).notifyItemInserted(9999999);
    }

    private boolean checkName() {
        String headingInput = name_error.getEditText().getText().toString().trim();
        if (headingInput.length() < 5) {
            name_error.setError("Minimum 5 characters");
            return false;
        } else {
            name_error.setError(null);
            return true;
        }
    }

    private boolean checkPhone() {
        String descInput = phone_error.getEditText().getText().toString().trim();
        if (descInput.length() != 10) {
            phone_error.setError("10 characters required");
            return false;
        } else {
            phone_error.setError(null);
            return true;
        }
    }

    private boolean checkLocation() {
        String priceInput = location_error.getEditText().getText().toString().trim();


        if (priceInput.isEmpty()) {
            location_error.setError("Required field");
            return false;
        } else {
            location_error.setError(null);
            return true;
        }

    }
}