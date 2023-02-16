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

public class FragmentForMyStorageOne extends Fragment {

    public static FragmentForMyStorageOne newInstance() {
        return new FragmentForMyStorageOne();
    }

    public static RecyclerView mRecyclerView1;
    private MyStorageRecyclerViewAdapter mAdapter1;

    private ProgressBar mProgressCircle1;

    private FirebaseAuth mFirebaseAuth1;

    private DatabaseReference mDatabaseRef1;
    private List<Book> mUploads1;
    private List<Book> mUploads2;
    private String mFirebaseUser1;
    private RelativeLayout empty;
    private int count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_fragment_for_my_storage_one, container, false);
        mRecyclerView1 = rootview.findViewById(R.id.recyclerview_id2);
        mRecyclerView1.setHasFixedSize(true);
        mRecyclerView1.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        empty = rootview.findViewById(R.id.empty_privacy1);
        mProgressCircle1 = rootview.findViewById(R.id.progress_globalstorage2);

        mUploads1 = new ArrayList<>();
        mUploads2 = new ArrayList<>();
        mFirebaseAuth1 = FirebaseAuth.getInstance();
        mFirebaseUser1 = mFirebaseAuth1.getCurrentUser().getUid();

        mDatabaseRef1 = FirebaseDatabase.getInstance().getReference("test");
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mDatabaseRef1.addValueEventListener(valueEventListener);
//            }
//        }, 400);


        return rootview;
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                mUploads1 = new ArrayList<>();
                mUploads2 = new ArrayList<>();
                for (DataSnapshot _data : dataSnapshot.getChildren()) {
                    Book book = _data.getValue(Book.class);
                    mUploads1.add(book);
                }
                for (int i = 0; i < mUploads1.size(); i++) {
                    if (mUploads1.get(i).getUserID().matches(mFirebaseUser1)) {
                        mUploads2.add(mUploads1.get(i));
                        count++;
                    }

                }
                mAdapter1 = new MyStorageRecyclerViewAdapter(getActivity(), mUploads2);
                mProgressCircle1.setVisibility(View.INVISIBLE);
                mRecyclerView1.setAdapter(mAdapter1);
                ((MyStorageRecyclerViewAdapter) mRecyclerView1.getAdapter()).notifyDataSetChanged();
                if (count == 0) {
                    empty.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getContext(),databaseError.getMessage(), Toast.LENGTH_LONG).show();
            mProgressCircle1.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mUploads1.clear();
        mUploads2.clear();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatabaseRef1.addValueEventListener(valueEventListener);
            }
        }, 400);

    }

    private void filterView() {

        mUploads2 = new ArrayList<>();
        for (int i = 0; i < mUploads1.size(); i++) {
            if (mUploads1.get(i).getName().contains("и")) {
                mUploads2.add(mUploads1.get(i));

            }

        }

        mAdapter1 = new MyStorageRecyclerViewAdapter(getActivity(), mUploads2);
        mRecyclerView1.setAdapter(mAdapter1);


    }
}
