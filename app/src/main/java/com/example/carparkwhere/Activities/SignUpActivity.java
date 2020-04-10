package com.example.carparkwhere.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carparkwhere.DAO.DAOImplementations.UserDataDaoFirebaseImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.UserDataDao;
import com.example.carparkwhere.Exceptions.UserNotLoggedInException;
import com.example.carparkwhere.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class SignUpActivity extends AppCompatActivity {

    EditText emailAddressSignUpEditText;
    EditText passwordSignUpEditText;
    EditText displayNameEditText;
    Button signUpButton;
    ProgressDialog nDialog;
    private UserDataDao userDataDaoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userDataDaoHelper = new UserDataDaoFirebaseImpl();

        emailAddressSignUpEditText = findViewById(R.id.signUpActivity_emailAddressEditText);
        passwordSignUpEditText = findViewById(R.id.signUpActivity_passwordEditText);
        displayNameEditText = findViewById(R.id.signUpActivity_displayNameEditText);
        signUpButton = findViewById(R.id.signUpButton);

        setupSignUpButton();

    }

    private void presentProgressDialog(String message){
        nDialog = new ProgressDialog(SignUpActivity.this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle(message);
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }

    private void setupSignUpButton(){



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (emailAddressSignUpEditText.getText().toString().isEmpty() || passwordSignUpEditText.getText().toString().isEmpty() || displayNameEditText.getText().toString().isEmpty()){
                    Toast.makeText(SignUpActivity.this,"Please fill up all the fields", Toast.LENGTH_SHORT).show();
                }else{
                    presentProgressDialog("Creating Account...");
                    userDataDaoHelper.createNewUser(SignUpActivity.this, emailAddressSignUpEditText.getText().toString(), passwordSignUpEditText.getText().toString(), displayNameEditText.getText().toString(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            nDialog.dismiss();
                            if (task.isSuccessful()){
                                try{
                                    userDataDaoHelper.updateDisplayName(displayNameEditText.getText().toString());
                                }catch (UserNotLoggedInException e){

                                }
                                Toast.makeText(SignUpActivity.this,"Sign up successful! Please check your email to verify ur account", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(SignUpActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


            }
        });
    }
}
