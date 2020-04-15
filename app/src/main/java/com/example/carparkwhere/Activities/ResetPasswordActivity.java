/**
 * This Java class is used for user to enter email address
 * and reset the account password.
 *
 * @authors Kai Jie, Swee Sen
 * @version 1.0
 */

package com.example.carparkwhere.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.carparkwhere.DAO.DAOImplementations.UserDataDaoFirebaseImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.UserDataDao;
import com.example.carparkwhere.R;

public class ResetPasswordActivity extends AppCompatActivity {

    private UserDataDao userDataDaoHelper;

    /**
     * onCreate is used to initialise the activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Link to XML file
        setContentView(R.layout.activity_reset_password);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#111111")));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Reset Password");

        // Textbox for the email address
        final EditText emailAddress_ET;
        // Buttons to confirm reset of password and cancel reset of password
        Button resetPasswordSubmit_BTN, resetPasswordCancel_BTN;
        final TextView checkPassword_TV = findViewById(R.id.checkPassword);

        userDataDaoHelper = new UserDataDaoFirebaseImpl();
        // Link to the text field
        emailAddress_ET = findViewById(R.id.emailAddress);

        try {
            emailAddress_ET.setText(userDataDaoHelper.getUserEmail());
        }catch (Exception e){

        }

        // Link to buttons
        resetPasswordCancel_BTN = findViewById(R.id.cancelButton);
        resetPasswordSubmit_BTN = findViewById(R.id.submitButton);

        // Submit button to confirm reset
        resetPasswordSubmit_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (emailAddress_ET.getText().toString().isEmpty()) // if no email address is provided by user
                    checkPassword_TV.setText("Enter email address.");
                else {
                    checkPassword_TV.setText("");
                    userDataDaoHelper.sendResetPasswordEmail(emailAddress_ET.getText().toString());
                    Toast.makeText(ResetPasswordActivity.this,"Sent to your email. Please Check" , Toast.LENGTH_SHORT).show();
                    finish();
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

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}