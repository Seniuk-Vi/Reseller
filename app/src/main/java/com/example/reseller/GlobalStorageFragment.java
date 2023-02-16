package com.example.reseller;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reseller.Adapters.RecyclerViewAdapter;
import com.example.reseller.Dialogs.DialogPopUpView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class GlobalStorageFragment extends Fragment implements DialogPopUpView.OnInputListener {
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<Book> mUploads;
    private List<Book> mUploads2;
    private List<Book> mUploads3;
    private List<Book> mUploads4;
    private List<Book> mUploads5;
    private List<Book> mUploads6;
    private Book book;

    DialogFragment dlg1;
    public static EditText mSearchField;
    private ImageButton mSearchBtn, mSettSort;
    private Dialog mDialog;
    private String searchtext = "";
    private int pricemin, pricemax;
    private boolean settingsPressed = false;
    private String category1, sorting1;
    private RelativeLayout empty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_globalstorage, container, false);
        mSearchField = rootview.findViewById(R.id.search_field);
        mSearchBtn = rootview.findViewById(R.id.search_btn);
        mRecyclerView = rootview.findViewById(R.id.recyclerview_id1);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        dlg1 = new DialogPopUpView();
        empty = rootview.findViewById(R.id.empty_privacy2);

        mProgressCircle = rootview.findViewById(R.id.progress_globalstorage);
        mUploads = new ArrayList<>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("test");
        mDialog = new Dialog(this.getContext());
        final Spinner spinner = rootview.findViewById(R.id.spinner_category);
        mSettSort = rootview.findViewById(R.id.settings_menu_global);


        mSettSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg1.setTargetFragment(GlobalStorageFragment.this, 1);
                dlg1.show(getFragmentManager(), "dlg1");

            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatabaseRef.addValueEventListener(valueEventListener);
                sortView();
            }
        }, 400);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                searchtext = mSearchField.getText().toString();
//                    sortView();
                filterView();
            }
        });


        return rootview;
    }


    //Це штука для передавання даних від DialogFragment + Сортування в DialogSort
    @Override
    public void sendInput(int minPrice, int maxPrice, String category, String sorting, boolean pressed, String searchtext) {
//        Log.d("GlobalStorageFragment", "sendInput: got the input: " + input);
//        if (minPrice != 0){
//        mUploads3 = new ArrayList<>();
//        pricemin = minPrice;
//        pricemax = maxPrice;
//        category1 = category;
//        sorting1 = sorting;
//        settingsPressed = pressed;
//        mSearchField.setText(minPrice + maxPrice + category + sorting + searchtext);

        // Search
        mUploads3 = new ArrayList<>();
        if (!searchtext.isEmpty()) {
            for (int i = 0; i < mUploads.size(); i++) {
                if (mUploads.get(i).getName().contains(searchtext)) {
                    mUploads3.add(mUploads.get(i));

                }

            }
        } else {
            mUploads3.addAll(mUploads);
        }

        // Min & Max price
        int min = minPrice;
        int max = maxPrice;
        if (maxPrice == 0) {
            max = (int) Double.POSITIVE_INFINITY;
        }

        mUploads4 = new ArrayList<>();
        if (min != 0 | max != 0) {
            for (int k = 0; k < mUploads3.size(); k++) {
                if (mUploads3.get(k).getPrice() > min - 1 && mUploads3.get(k).getPrice() < max - 1) {
                    mUploads4.add(mUploads3.get(k));
                }
            }


        } else mUploads4.addAll(mUploads3);

        //Category
        mUploads5 = new ArrayList<>();
        if (!category.equals("Category")) {
            if (!category.isEmpty()) {
                for (int a = 0; a < mUploads4.size(); a++) {
                    if (mUploads4.get(a).getCategory().contains(category)) {
                        mUploads5.add(mUploads4.get(a));
                    }

                }
            }
        } else {
            mUploads5.addAll(mUploads4);
        }
        // Sorting
        if (!sorting.equals("Sorting")) {
            switch (sorting) {
                case "Cheapest":
                    Collections.sort(mUploads5, Book.ByPrice);
                    break;
                case "The most expensive":
                    Collections.sort(mUploads5, Book.ByPrice);
                    Collections.reverse(mUploads5);
                    break;
                case "Newest":
                    Collections.reverse(mUploads5);
                    break;
                case "Recommended":
                    Collections.sort(mUploads5, Book.ByViews);
                    break;
            }
        }
        mAdapter = new RecyclerViewAdapter(getActivity(), mUploads5);
        mRecyclerView.setAdapter(mAdapter);
        if (mAdapter.getItemCount() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else empty.setVisibility(View.INVISIBLE);
    }

    // search by firebase (poor)
    private void firebaseUserSearch(String searchtext) {
        Query query = mDatabaseRef.orderByChild("name").startAt(searchtext).endAt(searchtext + "\uf8ff");

        query.addValueEventListener(valueEventListener);
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                mUploads = new ArrayList<>();
                for (DataSnapshot _data : dataSnapshot.getChildren()) {
                    book = _data.getValue(Book.class);
                    mUploads.add(book);
//                    Collections.sort(mUploads, new Comparator<Book>() {
//                        @Override
//                        public int compare(Book o1, Book o2) {
//                            return o1.getName().compareTo(o2.getName());
//                        }
//                    });


                }
//                Collections.shuffle(mUploads);
                mAdapter = new RecyclerViewAdapter(getActivity(), mUploads);
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

    private void sortView() {
//        Collections.sort(mUploads, Book.ByPrice);
//        mUploads2 = new ArrayList<>();
//        mUploads2 = mUploads;
//        for(int i = 0; i<mUploads.size();i++){
//
//            mUploads2.add(mUploads.get(i));
//            mUploads.get(i).getName();
//        }
        mAdapter = new RecyclerViewAdapter(getActivity(), mUploads);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void filterView() {

        mUploads2 = new ArrayList<>();
        if (!mSearchField.getText().toString().isEmpty()) {
            for (int i = 0; i < mUploads.size(); i++) {
                if (mUploads.get(i).getName().contains(mSearchField.getText().toString())) {
                    mUploads2.add(mUploads.get(i));

                }

            }
            mAdapter = new RecyclerViewAdapter(getActivity(), mUploads2);
            mRecyclerView.setAdapter(mAdapter);
        }


    }

//    private static void filterDialog(int minPrice, int maxPrice, String category, String sorting, String search){
//        mUploads3 = new ArrayList<>();
//        if(!search.isEmpty()){
//            for(int i = 0; i<mUploads.size();i++){
//                if(mUploads.get(i).getName().contains(mSearchField.getText().toString())){
//                    mUploads3.add(mUploads.get(i));
//
//                }
//
//            }
//
//            mAdapter = new RecyclerViewAdapter(getActivity(), mUploads3);
//            mRecyclerView.setAdapter(mAdapter);
//        }
//        mSearchField.setText(minPrice + maxPrice + category + sorting + search);
//    }
    //Берем розміри екрану

}

