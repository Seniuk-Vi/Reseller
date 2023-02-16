package com.example.reseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    ".{6,}" +               //at least 6 characters
                    "$");
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputName;

    EditText emailID, password, name;
    Button btnSignUp;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mFirebaseAuth = FirebaseAuth.getInstance();

        textInputEmail = findViewById(R.id.text_input_email_error);
        textInputPassword = findViewById(R.id.text_input_password_error);
        textInputName = findViewById(R.id.text_input_name_loginIn_error);
        emailID = findViewById(R.id.text_input_email);
        password = findViewById(R.id.text_input_password);
        tvSignUp = findViewById(R.id.tv);
        btnSignUp = findViewById(R.id.SignUping);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpGo();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
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
    }  private boolean checkName() {
        String nameInput = textInputName.getEditText().getText().toString().trim();
        if (nameInput.isEmpty()) {
            textInputName.setError("Required field");
            return false;
        } else if (nameInput.length()<5) {
            textInputName.setError("Minimum 5 characters");
            return false;
        } else
            textInputName.setError(null);
        return true;
    }

    public void SignUpGo() {
        final String email = emailID.getText().toString();
        String pwd = password.getText().toString();
        if (!checkEmail() | !checkPassword()|!checkName()) {
            return;
        }
        mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Try other Email!", Toast.LENGTH_SHORT ).show();
                } else {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Toast.makeText(SignUp.this, "SignUp Unsuccesful", Toast.LENGTH_SHORT);
                    final UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(textInputName.getEditText().getText().toString().trim())
                            .setPhotoUri(Uri.parse("https://image.flaticon.com/icons/png/512/149/149071.png"))
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                    } startActivity(new Intent(SignUp.this, SignIn.class));
                                    finish();
                                }
                            });


                }
            }
        });


    }

}
