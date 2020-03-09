package com.example.carparkwhere;

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
import com.example.carparkwhere.Utilities.FirebaseManager;
import com.example.carparkwhere.Utilities.ServerInterfaceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



import java.io.IOException;
import java.util.ArrayList;




public class SignInActivity extends AppCompatActivity {
    EditText emailAddressEditText;
    EditText passwordEditText;
    Button signInButton;
    Button createAccountButton;
    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        emailAddressEditText = findViewById(R.id.emailAddressSignInEditText);
        passwordEditText = findViewById(R.id.passwordSignInEditText);
        signInButton = findViewById(R.id.signInButton);
        createAccountButton = findViewById(R.id.createAccountButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseManager.signInWithEmail(SignInActivity.this, emailAddressEditText.getText().toString(), passwordEditText.getText().toString(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            if (FirebaseManager.getCurrentUser().isEmailVerified()){
                                Toast.makeText(SignInActivity.this,"Signed in succcessfully! (Supposed to transition to the main page)", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SignInActivity.this,"You havent verified your email!", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(SignInActivity.this,"Credentials are wrong. try again!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        animationView = findViewById(R.id.signInMainLottieAnimation);

        animationView.setAnimationFromUrl ("https://assets5.lottiefiles.com/packages/lf20_u3YlGl.json");

        Button testBTN1 = findViewById(R.id.testBTN1);
        testBTN1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,AccountOptionsActivity.class));
            }
        });

        Button testBTN2 = findViewById(R.id.testBTN2);
        testBTN2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this,MapsActivity.class));
            }
        });

        
//        Button testBTN3 = findViewById(R.id.testBTN3);
//        testBTN3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(SignInActivity.this, DetailCarparkActivity.class));
//            }
//        });

    }
}
