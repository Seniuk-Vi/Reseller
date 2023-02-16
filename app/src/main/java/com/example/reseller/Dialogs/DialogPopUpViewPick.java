package com.example.reseller.Dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.reseller.Adapters.PickerRecyclerVewAdapter;
import com.example.reseller.Adapters.RecyclerViewAdapter;
import com.example.reseller.Book;
import com.example.reseller.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DialogPopUpViewPick extends DialogFragment implements OnClickListener {
    public interface OnInputListener {
        void sendInput(String photo, String name, String category, String description, int price, String userId, String itemID);

    }

    public DialogPopUpViewPick.OnInputListener mInputListener;

    public interface Click{
        void onClick(

        );
    }

    private RecyclerView mRecyclerView;
    private PickerRecyclerVewAdapter mAdapter;
    private List<Book> mUploads;
    private Dialog mDialog;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mFirebaseAuth;
    private String username;



    //Передача в OrderingFragment
    public static String name2;
    public static String photo2;
    public static String category2;
    public static String description2;
    public static String userID2;
    public static String itemID2;
    public static int price2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.pop_view_pick, container);
        try {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        } catch (Exception e) {
            e.printStackTrace();
        }
        mRecyclerView = rootview.findViewById(R.id.recycler_picker);
        mRecyclerView.setHasFixedSize(true);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("test");
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));


        // Берем ім'я юзера
        mFirebaseAuth = FirebaseAuth.getInstance();
        username = mFirebaseAuth.getCurrentUser().getUid();
        mDatabaseRef.addValueEventListener(valueEventListener);
        mInputListener = (DialogPopUpViewPick.OnInputListener) getTargetFragment();


        return rootview;
    }


    public void onClick(View v) {


    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);


    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                mUploads = new ArrayList<>();
                for (DataSnapshot _data : dataSnapshot.getChildren()) {
                    Book book = _data.getValue(Book.class);
                    if(book.getUserID().equals(username)){
                        mUploads.add(book);
                    }
                    mAdapter = new PickerRecyclerVewAdapter(getActivity(), mUploads, new Click() {
                        @Override
                        public void onClick() {
                            mInputListener.sendInput(photo2,name2,category2,description2,price2,userID2,itemID2);
                            dismiss();
                        }
                    });

                }

                mRecyclerView.setAdapter(mAdapter);
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
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if (window == null) return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = 700;
        params.height = 1150;
        window.setAttributes(params);
    }


}