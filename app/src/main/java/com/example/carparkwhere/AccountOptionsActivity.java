package com.example.carparkwhere;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class AccountOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_options);

        ImageButton backAccountOptionsActivity_IMGBTN = findViewById(R.id.backAccountOptionsActivity_IMGBTN);
        backAccountOptionsActivity_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button viewUserBookmarks_BTN = findViewById(R.id.viewUserBookmarks_BTN);
        viewUserBookmarks_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button viewUserReviews_BTN = findViewById(R.id.viewUserReviews_BTN);
        viewUserReviews_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button signOutAccount_BTN = findViewById(R.id.signOutAccount_BTN);
        signOutAccount_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button viewResetPassword_BTN = findViewById(R.id.viewResetPassword_BTN);
        viewResetPassword_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button viewDeactivateAccount_BTN = findViewById(R.id.viewDeactivateAccount_BTN);
        viewDeactivateAccount_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
