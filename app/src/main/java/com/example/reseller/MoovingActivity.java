package com.example.reseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alexzh.circleimageview.CircleImageView;
import com.bumptech.glide.Glide;
import com.example.reseller.Adapters.UserRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoovingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    static boolean active = false;
    private FirebaseUser mFirebaseUser;
    private ImageView UsersPhoto;
    public static Toolbar toolbar;
    private String displayName;
    private NavigationView navigationView;
    public static RecyclerView mRecyclerView1;
    public static UserRecyclerViewAdapter mAdapter1;
    public TextView orders;
    private ProgressBar mProgressCircle1;

    private FirebaseAuth mFirebaseAuth1;

    private DatabaseReference mDatabaseRef1;
    private List<Book> mUploads1;
    private List<Book> mUploads2;
    private String mFirebaseUser1;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mooving);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        View logout = findViewById(R.id.nav_logout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent  = new Intent(MoovingActivity.this, SplashActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimary));
        toggle.getDrawerArrowDrawable().setBarLength(70f);
        toggle.getDrawerArrowDrawable().setBarThickness(6);
        toggle.getDrawerArrowDrawable().setGapSize(9);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);

        TextView navUsername = headerView.findViewById(R.id.user_name_nav_header);
//        final TextView userOrders = headerView.findViewById(R.id.view1);
        if (user != null) {
            displayName = user.getDisplayName();
            CircleImageView imageUser = headerView.findViewById(R.id.user_image_nav_header);
            TextView email = headerView.findViewById(R.id.email);
            email.setText(user.getEmail());
            if (!user.getPhotoUrl().equals("")) {

                Glide.with(this).load(user.getPhotoUrl()).centerCrop().into(imageUser);
            }
            navUsername.setText(displayName);
            String userID = user.getUid();
            mDatabaseRef1 = FirebaseDatabase.getInstance().getReference(userID + "Orders");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDatabaseRef1.addValueEventListener(valueEventListener);
                }
            }, 400);

//            FirebaseDatabase db = FirebaseDatabase.getInstance();
//            DatabaseReference ref = db.getReference(userID + "Orders");
//
//            ref.addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    long count = dataSnapshot.getChildrenCount();
//                    userOrders.setText("Orders: " + String.valueOf(count));
//
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//
//            });
        } else {
//            displayName = "tut";
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            drawer.closeDrawers();
            drawer.closeDrawer(navigationView);
        }


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new GlobalStorageFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_globalstorage);


        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_storage:

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction tr = fm.beginTransaction();
                tr.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.popup_enter, R.anim.pop_exit);
                tr.replace(R.id.fragment_container, new MyStorageFragment()).commit();
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyStorageFragment()).commit();
                toolbar.setTitle("My shop");
                break;

            case R.id.nav_globalstorage:

                FragmentManager fm1 = getSupportFragmentManager();
                FragmentTransaction tr1 = fm1.beginTransaction();
                tr1.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.popup_enter, R.anim.pop_exit);
                tr1.replace(R.id.fragment_container, new GlobalStorageFragment()).commit();

                toolbar.setTitle("Market");
                break;

            case R.id.nav_additem:

                FragmentManager fm2 = getSupportFragmentManager();
                FragmentTransaction tr2 = fm2.beginTransaction();
                tr2.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.popup_enter, R.anim.pop_exit);
                tr2.replace(R.id.fragment_container, new AddNewItemFragment()).commit();

                toolbar.setTitle("Add new lot");
                break;

            case R.id.nav_ordering:

                FragmentManager fm3 = getSupportFragmentManager();
                FragmentTransaction tr3 = fm3.beginTransaction();
                tr3.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.popup_enter, R.anim.pop_exit);
                tr3.replace(R.id.fragment_container, new OrderingFragment()).commit();

                toolbar.setTitle("Make order");
                break;

            case R.id.nav_notes:
                FragmentManager fm4 = getSupportFragmentManager();
                FragmentTransaction tr4 = fm4.beginTransaction();
                tr4.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.popup_enter, R.anim.pop_exit);
                tr4.replace(R.id.fragment_container, new NotesFragment()).commit();

                toolbar.setTitle("Orders");
                break;
            case R.id.nav_progress:

                FragmentManager fm5 = getSupportFragmentManager();
                FragmentTransaction tr5 = fm5.beginTransaction();
                tr5.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.popup_enter, R.anim.pop_exit);
                tr5.replace(R.id.fragment_container, new ProgressFragment()).commit();
                toolbar.setTitle("Settings");
                break;
            case R.id.nav_faq:
                Toast.makeText(this, "resellerservicehelp@gmail.com", Toast.LENGTH_LONG).show();
                break;

            case R.id.nav_settings:
                FragmentManager fm6 = getSupportFragmentManager();
                FragmentTransaction tr6 = fm6.beginTransaction();
                tr6.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.popup_enter, R.anim.pop_exit);
                tr6.replace(R.id.fragment_container, new SettingsFragment()).commit();

                toolbar.setTitle("Profile");
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                break;
        }
        drawer.closeDrawer(GravityCompat.START, true);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {

                mUploads1 = new ArrayList<>();
                for (DataSnapshot _data : dataSnapshot.getChildren()) {
                    Book book = _data.getValue(Book.class);
                    mUploads1.add(book);
                }
//                for (int i = 0; i < mUploads1.size(); i++) {
//                    if (mUploads1.get(i).getStatus().matches("TODO")) {
//                        mUploads2.add(mUploads1.get(i));
//                        count++;
//                    }
//
//                }
                View headerView = navigationView.getHeaderView(0);
                if (mUploads1.size() != 0){
                    orders = headerView.findViewById(R.id.view1);
                    orders.setText("Orders: " + String.valueOf(mUploads1.size()));

                }else orders.setText("Orders: 0");
            } catch (Exception e) {
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getContext(),databaseError.getMessage(), Toast.LENGTH_LONG).show();
            mProgressCircle1.setVisibility(View.INVISIBLE);
        }
    };

}

