package com.example.reseller;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.reseller.Adapters.MyStorageRecyclerViewAdapter;
import com.example.reseller.Adapters.RecyclerViewAdapter;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookMyStorage extends AppCompatActivity {
    private String heading, description, image, category, itemID;
    private int price;
    private boolean privacy;
    private EditText Heading, Description, Price;
    private ImageView Image;
    private Spinner spinner;
    private TextInputLayout HeadingError, DescriptionError, PriceError, textInputCat;
    private SwitchMaterial SwitchPrivasy;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Button Update, Delete;
    private RelativeLayout shureLay;
    private LinearLayout scrollView;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mystorage);
        Intent intent = getIntent();
        heading = intent.getExtras().getString("Title");
        description = intent.getExtras().getString("Description");
        image = intent.getExtras().getString("Image");
        category = intent.getExtras().getString("Category");
        price = intent.getExtras().getInt("Price");
        itemID = intent.getExtras().getString("ItemID");
        privacy = intent.getExtras().getBoolean("Privacy");
        Heading = findViewById(R.id.book_mystorage_heading);
        Description = findViewById(R.id.book_mystorage_desc);
        Price = findViewById(R.id.book_mystorage_price);
        Image = findViewById(R.id.book_mystorage_image);
        HeadingError = findViewById(R.id.book_mystorage_heading_error);
        DescriptionError = findViewById(R.id.book_mystorage_desc_error);
        PriceError = findViewById(R.id.book_mystorage_price_error);
        Update = findViewById(R.id.upload);
        Delete = findViewById(R.id.delete);
        textInputCat = findViewById(R.id.text_input_cat_ERROR);

        spinner = findViewById(R.id.book_spinner_category_add);
        SwitchPrivasy = findViewById(R.id.book_switch_privasy);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.Categories, R.layout.spinner_text);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (category.equals("The baby world")) {
            spinner.setSelection(1);
        }
        if (category.equals("Estate")) {
            spinner.setSelection(2);
        }
        if (category.equals("Transport")) {
            spinner.setSelection(3);
        }
        if (category.equals("Entertainment")) {
            spinner.setSelection(4);
        }
        if (category.equals("Spare parts for transport")) {
            spinner.setSelection(5);
        }
        if (category.equals("Work")) {
            spinner.setSelection(6);
        }
        if (category.equals("Animals")) {
            spinner.setSelection(7);
        }
        if (category.equals("House and garden")) {
            spinner.setSelection(8);
        }
        if (category.equals("Electronics")) {
            spinner.setSelection(9);
        }
        if (category.equals("Clothes")) {
            spinner.setSelection(10);
        }

        //setting into views
        Glide.with(this).load(image).into(Image);
        Heading.setText(heading);
        Description.setText(description);
        Price.setText(String.valueOf(price));

        if (privacy) {
            SwitchPrivasy.setChecked(true);
        }
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(BookMyStorage.this);
                dialog.setTitle("Delete");
                dialog.setMessage("Are you shure?");
                dialog.setCancelable(true);

// Specifying a listener allows you to take an action before dismissing the dialog.
// The dialog is automatically dismissed when a dialog button is clicked.
                dialog.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int
                                    id) {
                                // Continue with some operation
                                myRef = FirebaseDatabase.getInstance().getReference()
                                        .child("test").child(itemID);
                                myRef.removeValue();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((MyStorageRecyclerViewAdapter) FragmentForMyStorageOne.mRecyclerView1.getAdapter()).notifyDataSetChanged();
                                        ((MyStorageRecyclerViewAdapter) FragmentForMyStorageSec.mRecyclerView.getAdapter()).notifyDataSetChanged();
                                        finish();
                                    }
                                }, 3000);

                            }
                        });

// A null listener allows the button to dismiss the dialog and take no further action.

                dialog.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                                dialog.cancel();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 3000);
                                ((MyStorageRecyclerViewAdapter) FragmentForMyStorageOne.mRecyclerView1.getAdapter()).notifyDataSetChanged();
                                ((MyStorageRecyclerViewAdapter) FragmentForMyStorageSec.mRecyclerView.getAdapter()).notifyDataSetChanged();


                            }
                        });

                AlertDialog alert = dialog.create();
                alert.show();

            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem();

            }
        });

    }

    private void updateItem() {

        if (!checkDesc() | !checkHeading() | !checkPrice() | !checkCat()) {
            return;
        }
        //Heading
        myRef = FirebaseDatabase.getInstance().getReference().child("test").child(itemID).child("name");
        myRef.setValue(Heading.getText().toString());
        //Description
        myRef = FirebaseDatabase.getInstance().getReference().child("test").child(itemID).child("description");
        myRef.setValue(Description.getText().toString());
        //Price
        myRef = FirebaseDatabase.getInstance().getReference().child("test").child(itemID).child("price");
        myRef.setValue(Integer.parseInt(Price.getText().toString()));
        //Category
        myRef = FirebaseDatabase.getInstance().getReference().child("test").child(itemID).child("category");
        myRef.setValue(spinner.getSelectedItem().toString());
        if(SwitchPrivasy.isChecked()){
            myRef = FirebaseDatabase.getInstance().getReference().child("test").child(itemID).child("privacy");
            myRef.setValue(true);
        }

        Toast.makeText(getApplicationContext(), "Upload Success", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }

    private boolean checkHeading() {
        String headingInput = HeadingError.getEditText().getText().toString().trim();
        if (headingInput.length() < 5) {
            HeadingError.setError("Minimum 5 characters");
            return false;
        } else {
            HeadingError.setError(null);
            return true;
        }
    }

    private boolean checkDesc() {
        String descInput = DescriptionError.getEditText().getText().toString().trim();
        if (descInput.length() < 30) {
            DescriptionError.setError("Minimum 30 characters");
            return false;
        } else {
            DescriptionError.setError(null);
            return true;
        }
    }

    private boolean checkPrice() {
        String priceInput = PriceError.getEditText().getText().toString().trim();


        if (priceInput.isEmpty()) {
            PriceError.setError("Required field");
            return false;
        } else {
            PriceError.setError(null);
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
