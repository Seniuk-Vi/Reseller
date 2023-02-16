package com.example.reseller.Dialogs;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.reseller.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;


public class DialogEmail extends DialogFragment implements OnClickListener {
    public interface OnInputListener {
        void sendInput(int minPrice, int maxPrice, String category, String sorting, boolean pressed, String search);
    }
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    ".{6,}" +               //at least 6 characters
                    "$");
    public OnInputListener mInputListener;
    private TextInputLayout Name;
    private EditText name;
    private Button confirm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.popup_email, container);

        try {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        getDialog().getWindow().setWindowAnimations(R.style.DialogAnimation);


//        Name = rootview.findViewById(R.id.name_change_error);
        name = rootview.findViewById(R.id.pass_change);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();


        confirm = rootview.findViewById(R.id.confirmbtn);
        confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmail()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String newPassword = name.getText().toString();

                    user.updateEmail(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        dismiss();
                                    }
                                }
                            });
                }else Toast.makeText(getContext(),"Incorrect Email",Toast.LENGTH_SHORT).show();

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
    private boolean checkEmail() {

        String emailInput = name.getText().toString().trim();
        if (emailInput.isEmpty()) {
            name.setError("Required field");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            name.setError("It‘s incorrect email");
            return false;
        } else
            name.setError(null);
        return true;
    }


}