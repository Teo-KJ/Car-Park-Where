package com.example.carparkwhere.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carparkwhere.DAO.DAOImplementations.UserDataDaoFirebaseImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.UserDataDao;
import com.example.carparkwhere.FilesIdkWhereToPutYet.UserNotLoggedInException;
import com.example.carparkwhere.R;

public class ResetPasswordActivity extends AppCompatActivity {

    private UserDataDao userDataDaoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

//        final EditText emailAddress_ET, resetPasswordOld_ET, resetPasswordNew_ET;
        final EditText emailAddress_ET;
        Button resetPasswordSubmit_BTN, resetPasswordCancel_BTN;
        final TextView checkPassword_TV = findViewById(R.id.checkPassword);

        userDataDaoHelper = new UserDataDaoFirebaseImpl();

        emailAddress_ET = findViewById(R.id.emailAddress);
//        resetPasswordOld_ET = findViewById(R.id.oldPassword);
//        resetPasswordNew_ET = findViewById(R.id.newPassword);
        resetPasswordCancel_BTN = findViewById(R.id.cancelButton);
        resetPasswordSubmit_BTN = findViewById(R.id.submitButton);

        // Submit button to confirm reset
        resetPasswordSubmit_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailAddress_ET.getText().toString().isEmpty()){
                    checkPassword_TV.setText("Enter email address.");
                }
//                else if (resetPasswordOld_ET.getText().toString().isEmpty() | resetPasswordNew_ET.getText().toString().isEmpty()){
//                    checkPassword_TV.setText("Enter your password to continue.");
//                }
//                else if (!resetPasswordOld_ET.getText().toString().equals(resetPasswordNew_ET.getText().toString())){
//                    checkPassword_TV.setText("Passwords do not match.");
//                }
                else {
                    checkPassword_TV.setText("");
                    try{
                        userDataDaoHelper.sendResetPasswordEmail();
                        Toast.makeText(ResetPasswordActivity.this,"Sent to your email. Please Check" , Toast.LENGTH_SHORT).show();
                    }catch (UserNotLoggedInException e){

                    }

                }
            }
        });

        // Cancel button
        resetPasswordCancel_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
