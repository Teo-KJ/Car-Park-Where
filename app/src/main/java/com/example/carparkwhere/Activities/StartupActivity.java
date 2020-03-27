package com.example.carparkwhere.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.carparkwhere.DAO.DAOImplementations.CarparkDaoImpl;
import com.example.carparkwhere.DAO.DAOImplementations.UserDataDaoFirebaseImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.CarparkDao;
import com.example.carparkwhere.DAO.DAOInterfaces.UserDataDao;
import com.example.carparkwhere.FilesIdkWhereToPutYet.NetworkCallEventListener;
import com.example.carparkwhere.FilesIdkWhereToPutYet.UserNotLoggedInException;
import com.example.carparkwhere.R;
import com.google.android.gms.common.internal.FallbackServiceBroker;

public class StartupActivity extends AppCompatActivity {

    int SPLASH_TIME = 1500; //This is 3 seconds
    UserDataDao userDataDaoHelper;
    CarparkDao carparkDaoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        userDataDaoHelper = new UserDataDaoFirebaseImpl();
        carparkDaoHelper = new CarparkDaoImpl(this);

        getSupportActionBar().hide();
        carparkDaoHelper.getServerPrepared(new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartupActivity.this,SignInActivity.class);
                if (checkIfUserIsAlreadySignIn()){
                    intent = new Intent(StartupActivity.this,MapsActivity.class);
                }
                startActivity(intent);
                finish();

            }
        },SPLASH_TIME);
    }

    private boolean checkIfUserIsAlreadySignIn(){
        return ((userDataDaoHelper.isLoggedIn()) && (userDataDaoHelper.isEmailVerified()));
    }
}
