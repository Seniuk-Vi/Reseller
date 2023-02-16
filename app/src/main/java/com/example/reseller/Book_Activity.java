package com.example.reseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alexzh.circleimageview.CircleImageView;
import com.bumptech.glide.Glide;
import com.example.reseller.Adapters.BookAdapter;
import com.example.reseller.Adapters.RecyclerViewAdapter;
import com.example.reseller.Adapters.Slider_Pager_Adapter;

import com.example.reseller.Dialogs.DialogPopUpViewOrder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Book_Activity extends AppCompatActivity {

    private TextView tvtitle, tvdescription, tvcategory, tvPrice;
    private ImageView img;
    private DatabaseReference mDatabaseRef;
    private List<Book> mData;
    private RecyclerView mRecyclerView;

    Slider_Pager_Adapter sliderPagerAdapter;
    ArrayList<Integer> slider_image_list = new ArrayList<>();
    int page_position = 0;
    Timer timer;
    private ViewPager viewPager;
    private RelativeLayout pages_dots;
    private TextView[] dots;
    private String[] imageUrls = new String[1];
    private FloatingActionButton back;
    private List<Book> mUploads1;
    private List<Book> mUploads2;
    private String userID;
    private FirebaseUser user;
    private BookAdapter mAdapter1;
    private Context mContext;
    private ImageView image;
    //Order
    private String UserName, UserPhoto;
    private CircleImageView UserPhotoCircle;
    private TextView UserNameV, UserName2;
    //Make order
    private Button makeOrder;
    private DialogFragment dialog;
    private String userID2;
    private ImageView report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_);
        Intent intent = getIntent();
        final String Title = intent.getExtras().getString("Title");
        final String Description = intent.getExtras().getString("Description");
        final String thumbnail = intent.getExtras().getString("Thumbnail");
        final String Price = String.valueOf(intent.getExtras().getInt("Price"));
        final String itemID = intent.getExtras().getString("itemID");

        back = findViewById(R.id.back);
        tvtitle = (TextView) findViewById(R.id.txttitle);
        tvdescription = (TextView) findViewById(R.id.txtDesc);
        tvcategory = (TextView) findViewById(R.id.txtCat);
        tvPrice = findViewById(R.id.txtPrice);
//        pages_dots = findViewById(R.id.image_page_dots);
        mContext = this;
        image = findViewById(R.id.image_view);

        makeOrder = findViewById(R.id.MakeOrder);

        Glide.with(this).load(thumbnail).into(image);

        String category = intent.getExtras().getString("category");
        UserName = intent.getExtras().getString("UserName");
        UserPhoto = intent.getExtras().getString("UserPhoto");
        userID = intent.getExtras().getString("UserID");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("test");
        mDatabaseRef.addValueEventListener(valueEventListener);
        imageUrls[0] = thumbnail;
        //UserId set name
        tvcategory.setText(category);
        final Context context = this;
        userID2 = FirebaseAuth.getInstance().getUid();
        report = findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Report");
                dialog.setMessage("Are you shure?");
                dialog.setCancelable(true);

// Specifying a listener allows you to take an action before dismissing the dialog.
// The dialog is automatically dismissed when a dialog button is clicked.
                dialog.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference().child("test");
                                final DatabaseReference mCounterRef = mRootRef.child(itemID);
                                // listen for single change
                                mCounterRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        // getValue() returns Long
                                        long count = (long) dataSnapshot.child("count").getValue();

                                        mCounterRef.child("count").setValue(++count);  // <= Change to ++count

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }

                                });
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
//         viewPager = findViewById(R.id.image_page_slider);
//        Slider_Pager_Adapter adapter = new Slider_Pager_Adapter(this, imageUrls);
//        viewPager.setAdapter(adapter);
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                addBottomDots(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        // Присвоєння значення
        tvPrice.setText(Price + " $");
        tvtitle.setText(Title);
        tvdescription.setText(Description);

        //RecyclerView

        mRecyclerView = findViewById(R.id.recycler_in_book);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //Make order
        UserPhotoCircle = findViewById(R.id.UserPhoto);
        UserNameV = findViewById(R.id.UserName);
        UserName2 = findViewById(R.id.UserName2);
        Glide.with(this).load(Uri.parse(UserPhoto)).centerCrop().optionalFitCenter().into(UserPhotoCircle);
        UserNameV.setText(UserName);
        UserName2.setText(UserName + ": ");
        dialog = new DialogPopUpViewOrder();

        makeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("title", Title);
                args.putString("desc", Description);
                args.putString("image", thumbnail);
                args.putString("price", Price);
                args.putString("userID", userID);
                args.putString("itemID", itemID);
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });
        if (!itemID.isEmpty()) {
            final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference().child("test");
            final DatabaseReference mCounterRef = mRootRef.child(itemID);
            // listen for single change
            mCounterRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // getValue() returns Long
                    long count = (long) dataSnapshot.child("itemViews").getValue();

                    mCounterRef.child("itemViews").setValue(++count);  // <= Change to ++count

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }

    }


//    public void addBottomDots(int currentPage) {
//        dots = new TextView[imageUrls.length];
//        pages_dots.removeAllViews();
//        pages_dots.setPadding(0, 0, 0, 20);
//        for (int i = 0; i < dots.length; i++) {
//            dots[i] = new TextView(this);
//            dots[i].setText(Html.fromHtml("&#8226;"));
//            dots[i].setTextSize(35);
//            dots[i].setTextColor(Color.parseColor("#9f9f9f")); // un selected
//            pages_dots.addView(dots[i]);
//        }
//
//        if (dots.length > 0)
//            dots[currentPage].setTextColor(Color.parseColor("#2f383a")); // selected
//    }


    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                mUploads1 = new ArrayList<>();
                for (DataSnapshot _data : dataSnapshot.getChildren()) {
                    Book book = _data.getValue(Book.class);
                    if (book.getUserID().equals(userID2)){
                        mUploads1.add(book);

                    }



                }
                mAdapter1 = new BookAdapter(mContext, mUploads1);

                mRecyclerView.setAdapter(mAdapter1);
                ((RecyclerViewAdapter) mRecyclerView.getAdapter()).notifyDataSetChanged();

            } catch (Exception e) {
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getContext(),databaseError.getMessage(), Toast.LENGTH_LONG).show();
        }
    };
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}