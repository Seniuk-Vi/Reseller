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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.reseller.Adapters.UserRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Book_User_Activity_Process extends AppCompatActivity {

    private TextView name, phone, location;
    private ImageView img;
    private DatabaseReference mDatabaseRef;
    private List<Book> mData;
    private DatabaseReference myRef;
    private String itemID;
    private FloatingActionButton back;
    private Button endOrder;
    private String user;
    private TextView Title2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_process);


        name = findViewById(R.id.book_mystorage_name);
        phone = findViewById(R.id.book_mystorage_phone);
        location = findViewById(R.id.book_mystorage_location);
        img = (ImageView) findViewById(R.id.book_mystorage_image);


        back = findViewById(R.id.back);
        endOrder = findViewById(R.id.confirmPROCEss);
        Intent intent = getIntent();
        String Title = intent.getExtras().getString("Title");
        String Description = intent.getExtras().getString("Description");
        String image = intent.getExtras().getString("Image");
        itemID = intent.getExtras().getString("ItemID");
        Title2 = findViewById(R.id.txttitle);

        user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        endOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef = FirebaseDatabase.getInstance().getReference().child(user + "Orders").child(itemID).child("status");
                myRef.setValue("END");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Book_User_Activity_Process.this, "Order completed!", Toast.LENGTH_LONG).show();
                    }
                }, 2000);
                onBackPressed();
            }
        });
        // Присвоєння значення
        Title2.setText(Title);
        name.setText(intent.getExtras().getString("Name"));
        phone.setText(intent.getExtras().getString("Phone"));
        location.setText(intent.getExtras().getString("Location"));
        Glide.with(this).load(image).into(img);
    }

}