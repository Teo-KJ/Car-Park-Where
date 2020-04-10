package com.example.carparkwhere.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.carparkwhere.DAO.DAOImplementations.CarparkDaoImpl;
import com.example.carparkwhere.DAO.DAOImplementations.UserDataDaoFirebaseImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.CarparkDao;
import com.example.carparkwhere.DAO.DAOInterfaces.UserDataDao;
import com.example.carparkwhere.Exceptions.UserNotLoggedInException;
import com.example.carparkwhere.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;


public class SignInActivity extends AppCompatActivity {
    EditText emailAddressEditText;
    EditText passwordEditText;
    Button signInButton;
    Button createAccountButton;
    Button guessModeButton;
    Button resetPasswordButton;
    LottieAnimationView animationView;
    ProgressDialog nDialog;

    private CarparkDao carparkDaoHelper;
    private UserDataDao userDataDaoHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        carparkDaoHelper = new CarparkDaoImpl(this);
        userDataDaoHelper = new UserDataDaoFirebaseImpl();

        getSupportActionBar().hide();
        //checkIfUserIsAlreadySignIn();
        setupFindViewsByID();
        setupSignInButton();
        setupCreateAccountButton();
        setupAnimationView();
        setupGuessModeButton();
        setupResetPasswordButton();

        /*Button testBTN1 = findViewById(R.id.testBTN1);
        testBTN1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, UserBookmarksActivity.class));
            }
        });*/

    }

    private void checkIfUserIsAlreadySignIn(){
        try{
            if ((userDataDaoHelper.isLoggedIn()) && (userDataDaoHelper.isEmailVerified())){
                Toast.makeText(SignInActivity.this,"Successfully Signed In As " + userDataDaoHelper.getUserEmail(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignInActivity.this, MapsActivity.class);
                SignInActivity.this.startActivity ( intent );
            }
        }catch (UserNotLoggedInException e){
            //user is not logged in
        }

    }

    private void presentProgressDialog(String message){
        nDialog = new ProgressDialog(SignInActivity.this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle(message);
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }

    private void setupFindViewsByID(){
        emailAddressEditText = findViewById(R.id.emailAddressSignInEditText);
        passwordEditText = findViewById(R.id.passwordSignInEditText);
        signInButton = findViewById(R.id.signInButton);
        createAccountButton = findViewById(R.id.createAccountButton);
        guessModeButton = findViewById(R.id.guessModeButton);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
    }

    private void setupAnimationView(){
        animationView = findViewById(R.id.signInMainLottieAnimation);
        animationView.setAnimationFromUrl ("https://assets5.lottiefiles.com/packages/lf20_u3YlGl.json");
    }

    private void setupSignInButton(){
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((emailAddressEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty())){
                    Toast.makeText(SignInActivity.this,"Email and Password cannot be blank. Try again", Toast.LENGTH_SHORT).show();
                }else{
                    presentProgressDialog("Verifying Credentials...");
                    userDataDaoHelper.signInWithEmail(SignInActivity.this, emailAddressEditText.getText().toString(), passwordEditText.getText().toString(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            nDialog.dismiss();
                            if (task.isSuccessful()){
                                if (userDataDaoHelper.isEmailVerified()){

                                    Toast.makeText(SignInActivity.this,"Successfully signed in!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignInActivity.this, MapsActivity.class);
                                    SignInActivity.this.startActivity ( intent );
                                    finish();
                                }else{
                                    Toast.makeText(SignInActivity.this,"You havent verified your email!", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(SignInActivity.this,"Credentials are wrong. try again!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }

    private void setupCreateAccountButton(){
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    private void setupGuessModeButton(){
        guessModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presentProgressDialog("Loading");
                Intent i = new Intent(SignInActivity.this, MapsActivity.class);
                startActivity(i);
                finish();
                nDialog.dismiss();
            }
        });
    }

    private void setupResetPasswordButton(){
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, ResetPasswordActivity.class));
            }
        });
    }





}
