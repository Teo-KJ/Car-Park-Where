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
import com.example.carparkwhere.Exceptions.UserNotLoggedInException;
import com.example.carparkwhere.R;

/*
 * This class implements the AccountOptionsActivity. This is used to handle the interactions of the user with the user interface.
 *
 * @author Tay Jaslyn, Koh Swee Sen
 * */
public class AccountOptionsActivity extends AppCompatActivity {

    private TextView accountEmailTitle_TV;
    private Button signInSettings_BTN;
    Button viewUserBookmarks_BTN;
    ImageButton backAccountOptionsActivity_IMGBTN;
    Button viewUserReviews_BTN;
    Button signOutAccount_BTN;
    Button viewResetPassword_BTN;
    Button viewDeactivateAccount_BTN;

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

        //initialize buttons from the user interface.
        backAccountOptionsActivity_IMGBTN = findViewById(R.id.backAccountOptionsActivity_IMGBTN);
        viewUserBookmarks_BTN = findViewById(R.id.viewUserBookmarks_BTN);
        accountEmailTitle_TV = findViewById(R.id.accountEmailTitle_TV);
        viewUserReviews_BTN = findViewById(R.id.viewUserReviews_BTN);
        signOutAccount_BTN = findViewById(R.id.signOutAccount_BTN);
        viewResetPassword_BTN = findViewById(R.id.viewResetPassword_BTN);
        viewDeactivateAccount_BTN = findViewById(R.id.viewDeactivateAccount_BTN);

        signInSettings_BTN = findViewById(R.id.signInSettings_BTN);
        signInSettings_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountOptionsActivity.this,SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });



        //set back button.
        backAccountOptionsActivity_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        viewUserBookmarks_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        viewUserReviews_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountOptionsActivity.this, UserReviewsActivity.class));
            }
        });


        signOutAccount_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    userDataDaoHelper.signOut();
                    Intent intent = new Intent(AccountOptionsActivity.this,SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }catch (UserNotLoggedInException e){
                    //user not logged in
                }

                finish();
            }
        });


        viewResetPassword_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountOptionsActivity.this, ResetPasswordActivity.class));
            }
        });


        viewDeactivateAccount_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        try{
            accountEmailTitle_TV.setText("Signed-in as " + userDataDaoHelper.getUserEmail());
            signInSettings_BTN.setVisibility(View.INVISIBLE);
        }catch (UserNotLoggedInException e){
            accountEmailTitle_TV.setText("Not Signed-in");
            setButtonsHiddenIfNotSignedIn();
        }


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setButtonsHiddenIfNotSignedIn(){
        viewUserReviews_BTN.setVisibility(View.INVISIBLE);
        signOutAccount_BTN.setVisibility(View.INVISIBLE);
        viewResetPassword_BTN.setVisibility(View.INVISIBLE);
    }


}
