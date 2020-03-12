package com.example.carparkwhere;

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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.carparkwhere.Models.CarparkJson;
import com.example.carparkwhere.Models.Review;
import com.example.carparkwhere.Utilities.CarparkReviewsDataManager;
import com.example.carparkwhere.Utilities.FirebaseManager;
import com.example.carparkwhere.Utilities.NetworkCallEventListener;
import com.example.carparkwhere.Utilities.ServerInterfaceManager;
import com.example.carparkwhere.Utilities.UserDataManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



import java.io.IOException;
import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity {
    EditText emailAddressEditText;
    EditText passwordEditText;
    Button signInButton;
    Button createAccountButton;
    Button guessModeButton;
    LottieAnimationView animationView;
    ProgressDialog nDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

//        checkIfUserIsAlreadySignIn();
        setupFindViewsByID();
        setupSignInButton();
        setupCreateAccountButton();
        setupAnimationView();
        setupGuessModeButton();

        Button testBTN1 = findViewById(R.id.testBTN1);
        testBTN1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, AccountOptionsActivity.class));
            }
        });

//        Button testBTN2 = findViewById(R.id.testBTN2);
//        testBTN2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SignInActivity.this,MapsActivity.class));
//            }
//        });

    }

    private void checkIfUserIsAlreadySignIn(){
        FirebaseUser user = FirebaseManager.getCurrentUser();
        boolean isEmailVerified = user.isEmailVerified();

        if ((user != null) && (isEmailVerified)){
            Toast.makeText(SignInActivity.this,"Successfully Signed In As " + user.getEmail(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignInActivity.this, MapsActivity.class);
            SignInActivity.this.startActivity ( intent );
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
                    FirebaseManager.signInWithEmail(SignInActivity.this, emailAddressEditText.getText().toString(), passwordEditText.getText().toString(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            nDialog.dismiss();
                            if (task.isSuccessful()){
                                if (FirebaseManager.getCurrentUser().isEmailVerified()){
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
}
