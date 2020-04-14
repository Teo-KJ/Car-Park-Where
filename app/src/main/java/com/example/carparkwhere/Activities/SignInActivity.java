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


/**
 * This is the activity for user to sign in
 */
public class SignInActivity extends AppCompatActivity {

    /**
     * This editText is to input email address
     */
    EditText emailAddressEditText;

    /**
     * This editText to input password
     */
    EditText passwordEditText;

    /**
     * This button to sign in
     */
    Button signInButton;

    /**
     * This button to sign up
     */
    Button createAccountButton;

    /**
     * This button to enter the app as guest
     */
    Button guestModeButton;

    /**
     * This button to reset password
     */
    Button resetPasswordButton;

    /**
     * This is the animation view in the activity
     */
    LottieAnimationView animationView;

    /**
     * This is the progress dialog to be shown while waiting for network call result
     */
    ProgressDialog nDialog;

    /**
     * The carparkDao class
     */
    private CarparkDao carparkDaoHelper;

    /**
     * The userDao class
     */
    private UserDataDao userDataDaoHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        carparkDaoHelper = new CarparkDaoImpl(this);
        userDataDaoHelper = new UserDataDaoFirebaseImpl();

        getSupportActionBar().hide();
        checkIfUserIsAlreadySignIn();
        setupFindViewsByID();
        setupSignInButton();
        setupCreateAccountButton();
        setupAnimationView();
        setupGuestModeButton();
        setupResetPasswordButton();
    }

    /**
     * This function helps to check if user is logged in. If user is logged in, then automatically go to next activity
     */
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

    /**
     * This function helps to present the progress dialog while waiting for network call result
     * @param message the message to be shown in the progress dialog
     */
    private void presentProgressDialog(String message){
        nDialog = new ProgressDialog(SignInActivity.this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle(message);
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }

    /**
     * This function helps to set up findviewbyid for all the ui elements
     */
    private void setupFindViewsByID(){
        emailAddressEditText = findViewById(R.id.emailAddressSignInEditText);
        passwordEditText = findViewById(R.id.passwordSignInEditText);
        signInButton = findViewById(R.id.signInButton);
        createAccountButton = findViewById(R.id.createAccountButton);
        guestModeButton = findViewById(R.id.guessModeButton);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
    }

    /**
     * This function helps to set the animation for the lottie animation view
     */
    private void setupAnimationView(){
        animationView = findViewById(R.id.signInMainLottieAnimation);
        animationView.setAnimationFromUrl ("https://assets5.lottiefiles.com/packages/lf20_u3YlGl.json");
    }

    /**
     * This function helps to set up the functionality of the sign in button
     */
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

    /**
     * This function helps to setup the functionality of the sign up button
     */
    private void setupCreateAccountButton(){
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * This function helps to setup the functionality of the guest mode button
     */
    private void setupGuestModeButton(){
        guestModeButton.setOnClickListener(new View.OnClickListener() {
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

    /**
     * This function helps to setup the functionality of the reset password button
     */

    private void setupResetPasswordButton(){
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, ResetPasswordActivity.class));
            }
        });
    }





}
