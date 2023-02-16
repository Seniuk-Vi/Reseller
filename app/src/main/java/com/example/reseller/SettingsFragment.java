package com.example.reseller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alexzh.circleimageview.CircleImageView;
import com.bumptech.glide.Glide;
import com.example.reseller.Adapters.RecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {
    private DatabaseReference mDatabaseRef;
    private List<Book> mUploads, mUploads1;
    private Book book;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private ProgressBar mProgressCircle;
    private RelativeLayout empty;
    private String userID;
    private int lot , purch , views ;
    private TextView Lot, Purch, Views, userName;
    private CircleImageView imageUser;
    private String mFirebaseUser1;
    private FirebaseAuth mFirebaseAuth1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

//        btnLogOut = rootView.findViewById(R.id.log_Out);
//        btnLogOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//
//                Intent intent  = new Intent(getContext(), SplashActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                getActivity().finish();
//
//            }
//        });
        userName = rootView.findViewById(R.id.name_prof);
        imageUser = rootView.findViewById(R.id.image_prof);
        Lot = rootView.findViewById(R.id.lot);
        Purch = rootView.findViewById(R.id.purch);
        Views = rootView.findViewById(R.id.views);
        mRecyclerView = rootView.findViewById(R.id.recyclerview_id3);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.setHasFixedSize(true);

        mProgressCircle = rootView.findViewById(R.id.progress_globalstorage3);
        empty = rootView.findViewById(R.id.empty_privacy2);
        mFirebaseAuth1 = FirebaseAuth.getInstance();
        mFirebaseUser1 = mFirebaseAuth1.getCurrentUser().getUid();
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("test");
        lot = 0;
        views = 0;
        purch = 0;



        userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        Glide.with(getContext()).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(imageUser);
        return rootView;
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                lot = 0;
                views = 0;
                purch = 0;
                mUploads = new ArrayList<>();
                mUploads1 = new ArrayList<>();
                for (DataSnapshot _data : dataSnapshot.getChildren()) {
                    Book book = _data.getValue(Book.class);
                    mUploads.add(book);
                }
                for (int i = 0; i < mUploads.size(); i++) {
                    if (mUploads.get(i).getUserID().matches(userID)) {
                        mUploads1.add(mUploads.get(i));
                        lot++;
                        purch = purch + mUploads.get(i).getItemOrders();
                        views = views + mUploads.get(i).getItemViews();
                    }

                }

                Lot.setText(String.valueOf(lot));
                Purch.setText(String.valueOf(purch));
                Views.setText(String.valueOf(views));
                mAdapter = new RecyclerViewAdapter(getActivity(), mUploads1);
                mProgressCircle.setVisibility(View.INVISIBLE);
                mRecyclerView.setAdapter(mAdapter);
                ((RecyclerViewAdapter) mRecyclerView.getAdapter()).notifyDataSetChanged();
                if (mAdapter.getItemCount() == 0) {
                    empty.setVisibility(View.VISIBLE);
                } else empty.setVisibility(View.INVISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getContext(),databaseError.getMessage(), Toast.LENGTH_LONG).show();
            mProgressCircle.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatabaseRef.addValueEventListener(valueEventListener);

            }
        }, 400);
    }
}
