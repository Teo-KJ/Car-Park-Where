package com.example.carparkwhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carparkwhere.Utilities.FirebaseManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignUpActivity extends AppCompatActivity {

    EditText emailAddressSignUpEditText;
    EditText passwordSignUpEditText;
    Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailAddressSignUpEditText = findViewById(R.id.emailAddressSignUpEditText);
        passwordSignUpEditText = findViewById(R.id.passwordSignUpEditText);
        signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseManager.createNewUser(SignUpActivity.this, emailAddressSignUpEditText.getText().toString(), passwordSignUpEditText.getText().toString(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            Toast.makeText(SignUpActivity.this,"Sign up successful! Please check your email to verify ur account", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(SignUpActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }
}
