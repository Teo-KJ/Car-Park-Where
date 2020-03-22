package com.example.carparkwhere.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.carparkwhere.DAO.DAOImplementations.UserDataDaoFirebaseImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.UserDataDao;
import com.example.carparkwhere.FilesIdkWhereToPutYet.UserNotLoggedInException;
import com.example.carparkwhere.R;

public class AccountOptionsActivity extends AppCompatActivity {

    private TextView accountEmailTitle_TV;
    private UserDataDao userDataDaoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_options);

        userDataDaoHelper = new UserDataDaoFirebaseImpl();


        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#111111")));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Settings");


        accountEmailTitle_TV = findViewById(R.id.accountEmailTitle_TV);
        try{
            accountEmailTitle_TV.setText("Signed-in as " + userDataDaoHelper.getUserEmail());
        }catch (UserNotLoggedInException e){
            accountEmailTitle_TV.setText("Not Signed-in");
        }


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
                try{
                    userDataDaoHelper.signOut();
                }catch (UserNotLoggedInException e){
                    //user not logged in
                }

                finish();
            }
        });

        Button viewResetPassword_BTN = findViewById(R.id.viewResetPassword_BTN);
        viewResetPassword_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountOptionsActivity.this, ResetPasswordActivity.class));
            }
        });

        Button viewDeactivateAccount_BTN = findViewById(R.id.viewDeactivateAccount_BTN);
        viewDeactivateAccount_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
