package com.example.carparkwhere;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SubmitReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_review);

        ImageButton reviewBack_IMGBTN = findViewById(R.id.reviewBack_IMGBTN);
        reviewBack_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button reviewSave_BTN = findViewById(R.id.reviewSave_BTN);
        reviewSave_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button reviewDelete_BTN = findViewById(R.id.reviewDelete_BTN);
        reviewDelete_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void processReviewSubmission(){
        //JUST DO IT.
    }

}
