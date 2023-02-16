package com.example.reseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignIn extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    ".{6,}" +               //at least 6 characters
                    "$");
    public TextInputLayout textInputEmail;
    public TextInputLayout textInputPassword;
    EditText emailID, password;
    Button btnSignIn;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;
    private DatabaseReference mRef;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mFirebaseAuth = FirebaseAuth.getInstance();
        textInputEmail = findViewById(R.id.text_input_email_loginIn_error);
        textInputPassword = findViewById(R.id.text_input_password_loginIn_error);
        emailID = findViewById(R.id.text_input_email_loginIn);
        password = findViewById(R.id.text_input_password_loginIn);
        tvSignIn = findViewById(R.id.tvLog);
        btnSignIn = findViewById(R.id.SignInging);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (mFirebaseUser != null) {
                    Toast.makeText(SignIn.this, "You are logged in", Toast.LENGTH_SHORT);
                    Intent i = new Intent(SignIn.this, MoovingActivity.class);

                    startActivity(i);
                    finish();


                } else {
                    Toast.makeText(SignIn.this, "Please Login", Toast.LENGTH_SHORT);

                }
            }
        };
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               comfirmInput();
            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignIn.this, SignUp.class);
                startActivity(i);
                finish();
            }
        });
    }

    private boolean checkEmail() {

        String emailInput = textInputEmail.getEditText().getText().toString().trim();
        if (emailInput.isEmpty()) {
            textInputEmail.setError("Required field");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            textInputEmail.setError("It‘s uncorrect email");
            return false;
        } else
            textInputEmail.setError(null);
        return true;
    }


    private boolean checkPassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            textInputPassword.setError("Required field");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            textInputPassword.setError("Too weak password");
            return false;
        } else
            textInputPassword.setError(null);
        return true;
    }

    public void comfirmInput() {

//        if (!checkEmail() | !checkPassword()) {
//            return;
//        }
        String email = emailID.getText().toString();
        String pwd = password.getText().toString();

        mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(SignIn.this, "Login Error, Please Login Again", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intToHome = new Intent(SignIn.this, MoovingActivity.class);
                    startActivity(intToHome);
                    finish();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

}
