package com.example.carparkwhere;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.carparkwhere.Utilities.CarparkReviewsDataManager;

public class SubmitReviewActivity extends AppCompatActivity {

    RatingBar reviewRating_RBAR;
    TextView reviewComment_ET;
    String carparkId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_review);
        reviewRating_RBAR = findViewById(R.id.reviewRating_RBAR);
        reviewComment_ET = findViewById(R.id.reviewComment_ET);
        carparkId = getIntent().getStringExtra("carparkid");


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
                processReviewSubmission();
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
        CarparkReviewsDataManager.addNewReview(carparkId,Double.valueOf(reviewRating_RBAR.getRating()),reviewComment_ET.getText().toString());
        if (reviewRating_RBAR.getRating() != 0 && !reviewComment_ET.getText().toString().isEmpty()){

        }
    }

}
