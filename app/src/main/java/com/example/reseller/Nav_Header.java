package com.example.reseller;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Nav_Header extends AppCompatActivity {


    private TextView UserNameTV;
    private ImageView UserImageIV;

    private TextView UserName;
    private ImageView UserImage;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header);
        UserNameTV = findViewById(R.id.user_name_nav_header);
        UserImageIV = findViewById(R.id.user_image_nav_header);

        UserName.setText(UserName.toString());


    }
}
