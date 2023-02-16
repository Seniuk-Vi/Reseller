package com.example.reseller;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.reseller.Adapters.MyStorageRecyclerViewAdapter;
import com.example.reseller.Adapters.RecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentForMyStorageSec extends Fragment {
    public static RecyclerView mRecyclerView;
    private MyStorageRecyclerViewAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private FirebaseAuth mFirebaseAuth;

    private DatabaseReference mDatabaseRef;
    private List<Book> mUploads;
    private List<Book> mUploads2;
    private String mFirebaseUser;
    private RelativeLayout empty;
    private int count = 0;

    public static FragmentForMyStorageSec newInstance() {
        return new FragmentForMyStorageSec();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_fragment_for_my_storage_sec, container, false);
        mRecyclerView = rootview.findViewById(R.id.recyclerview_id3);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        mProgressCircle = rootview.findViewById(R.id.progress_globalstorage3);

        mUploads = new ArrayList<>();
        mUploads2 = new ArrayList<>();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser().getUid();
        empty = rootview.findViewById(R.id.empty_privacy);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("test");
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mDatabaseRef.addValueEventListener(valueEventListener);
//            }
//        }, 400);


        return rootview;
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                mUploads = new ArrayList<>();
                for (DataSnapshot _data : dataSnapshot.getChildren()) {
                    Book book = _data.getValue(Book.class);
                    mUploads.add(book);
                }
                for (int i = 0; i < mUploads.size(); i++) {
                    if (mUploads.get(i).getUserID().matches(mFirebaseUser) && mUploads.get(i).isPrivacy()) {
                        mUploads2.add(mUploads.get(i));
                        count++;
                    }

                }
                mAdapter = new MyStorageRecyclerViewAdapter(getActivity(), mUploads2);
                mProgressCircle.setVisibility(View.INVISIBLE);
                mRecyclerView.setAdapter(mAdapter);
                ((MyStorageRecyclerViewAdapter) mRecyclerView.getAdapter()).notifyDataSetChanged();
                if (count == 0) {
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
        mAdapter = new MyStorageRecyclerViewAdapter(getActivity(), mUploads2);
        mRecyclerView.setAdapter(mAdapter);

    }
}
