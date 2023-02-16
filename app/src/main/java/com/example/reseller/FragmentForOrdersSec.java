package com.example.reseller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reseller.Adapters.MyStorageRecyclerViewAdapter;
import com.example.reseller.Adapters.UserRecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentForOrdersSec extends Fragment {
    public static RecyclerView mRecyclerView;
    private UserRecyclerViewAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseAuth mFirebaseAuth;

    private DatabaseReference mDatabaseRef;
    private List<Book> mUploads;
    private List<Book> mUploads2;
    private String mFirebaseUser;
    private RelativeLayout empty;
    private int count = 0;

    public static FragmentForOrdersSec newInstance() {
        return new FragmentForOrdersSec();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_fragment_for_order_sec, container, false);
        mRecyclerView = rootview.findViewById(R.id.recyclerview_id3);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        mProgressCircle = rootview.findViewById(R.id.progress_globalstorage3);

        mUploads = new ArrayList<>();
        mUploads2 = new ArrayList<>();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser().getUid();
        empty = rootview.findViewById(R.id.empty_privacy);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser().getUid();
        String order = mFirebaseUser + "Orders";
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(order);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mDatabaseRef.addValueEventListener(valueEventListener);
//            }
//        }, 400);



        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAdapter.notifyDataSetChanged();
    }

    public  ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {

                for (DataSnapshot _data : dataSnapshot.getChildren()) {
                    Book book = _data.getValue(Book.class);
                    mUploads.add(book);
                }
                for (int i = 0; i < mUploads.size(); i++) {
                    if (mUploads.get(i).getStatus().matches("PROCESSING")) {
                        mUploads2.add(mUploads.get(i));
                        count++;
                    }

                }
                mAdapter = new UserRecyclerViewAdapter(getActivity(), mUploads2);
                mProgressCircle.setVisibility(View.INVISIBLE);
                mRecyclerView.setAdapter(mAdapter);
                ((UserRecyclerViewAdapter) mRecyclerView.getAdapter()).notifyDataSetChanged();
                if (mUploads2.isEmpty()) {
                    empty.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
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
        mUploads.clear();
        mUploads2.clear();
                new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatabaseRef.addValueEventListener(valueEventListener);
            }
        }, 400);

    }

    private void filterView() {

        mUploads2 = new ArrayList<>();
        for (int i = 0; i < mUploads.size(); i++) {
            if (mUploads.get(i).getName().contains("и")) {
                mUploads2.add(mUploads.get(i));

            }

        }
        mAdapter = new UserRecyclerViewAdapter(getActivity(), mUploads2);
        mRecyclerView.setAdapter(mAdapter);


    }
}
