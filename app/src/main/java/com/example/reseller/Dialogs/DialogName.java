package com.example.reseller.Dialogs;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.reseller.R;
import com.example.reseller.SignIn;
import com.example.reseller.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class DialogName extends DialogFragment implements OnClickListener {
    public interface OnInputListener {
        void sendInput(int minPrice, int maxPrice, String category, String sorting, boolean pressed, String search);
    }

    public OnInputListener mInputListener;
    private TextInputLayout Name;
    private EditText name;
    private Button confirm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.popup_name, container);

        try {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        getDialog().getWindow().setWindowAnimations(R.style.DialogAnimation);


        Name = rootview.findViewById(R.id.name_change_error);
        name = rootview.findViewById(R.id.name_change);


        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        name.setText(userName);
        confirm = rootview.findViewById(R.id.confirmbtn);
        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Name.getEditText().getText().toString().length()>4){
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(Name.getEditText().getText().toString().trim())
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        dismiss();
                                    }
                                }
                            });
                }else  Name.setError("min 5 char");


            }
        });

//                mInputListener.sendInput(MinPriceText, MaxPriceText, categoryText, sortingText, pressed, SearchField.getText().toString());


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


}