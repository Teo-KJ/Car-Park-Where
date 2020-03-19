package com.example.carparkwhere;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carparkwhere.Models.Carpark;
import com.example.carparkwhere.Models.Review;
import com.example.carparkwhere.Utilities.CarparkReviewsDataManager;
import com.example.carparkwhere.Utilities.NetworkCallEventListener;
import com.example.carparkwhere.Utilities.ServerInterfaceManager;
import com.example.carparkwhere.Utilities.UserDataManager;

import java.util.ArrayList;

public class SubmitReviewActivity extends AppCompatActivity {

    RatingBar reviewRating_RBAR;
    TextView reviewComment_ET;
    TextView reviewLocation_TV;
    ImageButton reviewBack_IMGBTN;
    Button reviewDelete_BTN;
    ProgressDialog nDialog;
    public String carparkId = "";
    public Boolean isEditingReview = false;
    public Review oldReview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_review);
        reviewRating_RBAR = findViewById(R.id.reviewRating_RBAR);
        reviewComment_ET = findViewById(R.id.reviewComment_ET);
        reviewLocation_TV = findViewById(R.id.reviewLocation_TV);
        carparkId = getIntent().getStringExtra("carparkid");

        ServerInterfaceManager.getCarparkDetailsByID(this, carparkId, new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                if (isSuccessful){
                    Carpark carpark = (Carpark) networkCallResult;
                    reviewLocation_TV.setText(carpark.getCarparkName());
                }
            }
        });


        reviewBack_IMGBTN = findViewById(R.id.reviewBack_IMGBTN);
        reviewBack_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Button reviewSave_BTN = findViewById(R.id.reviewSave_BTN);
        reviewSave_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processReviewSubmission();
            }
        });

        reviewDelete_BTN = findViewById(R.id.reviewDelete_BTN);
        reviewDelete_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processReviewDeletion();
            }
        });
        reviewDelete_BTN.setVisibility(View.INVISIBLE);


        ServerInterfaceManager.getCarparkReviewsByUserEmail(this, UserDataManager.getUserEmail(), new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                if (isSuccessful){
                    ArrayList<Review> existingReviews = (ArrayList<Review>) networkCallResult;
                    for (Review review:existingReviews){
                        if (review.getCarparkId().equals(carparkId)){
                            oldReview = existingReviews.get(0);
                            isEditingReview = true;
                            reviewRating_RBAR.setRating(oldReview.getRating().floatValue());
                            reviewComment_ET.setText(oldReview.getComment());
                            reviewDelete_BTN.setVisibility(View.VISIBLE);
                            reviewSave_BTN.setText("UPDATE");
                        }
                    }
                }
            }
        });




    }

    private void presentProgressDialog(String message){
        nDialog = new ProgressDialog(this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle(message);
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }

    public void processReviewDeletion(){

        presentProgressDialog("Deleting Review");

        ServerInterfaceManager.deleteCarparkReviewByReviewID(this, oldReview.get_id(), new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                nDialog.dismiss();
                if (isSuccessful){
                    Toast.makeText(SubmitReviewActivity.this,"Successfully Deleted!", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(SubmitReviewActivity.this,"Please check your network and try again!" + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void processReviewSubmission(){

        presentProgressDialog("Submitting");

        if (isEditingReview){
            Review newReview = new Review(UserDataManager.getUserEmail(),Double.valueOf(reviewRating_RBAR.getRating()),carparkId,reviewComment_ET.getText().toString(),UserDataManager.getDisplayName());
            ServerInterfaceManager.updateCarparkReviewWithNewValues(this, oldReview.get_id(), newReview, new NetworkCallEventListener() {
                @Override
                public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                    nDialog.dismiss();
                    if (isSuccessful){
                        Toast.makeText(SubmitReviewActivity.this,"Successfully Updated!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(SubmitReviewActivity.this,"Please check your network and try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        else{
            if (reviewRating_RBAR.getRating() != 0 && !reviewComment_ET.getText().toString().isEmpty()){
                Review newReview = new Review(UserDataManager.getUserEmail(),Double.valueOf(reviewRating_RBAR.getRating()),carparkId,reviewComment_ET.getText().toString(),UserDataManager.getDisplayName());
                ServerInterfaceManager.saveNewCarparkReview(this, newReview, new NetworkCallEventListener() {
                    @Override
                    public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                        nDialog.dismiss();
                        if (isSuccessful){
                            Toast.makeText(SubmitReviewActivity.this,"Successfully Submitted!", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            Toast.makeText(SubmitReviewActivity.this,"Please check your network and try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }


    }

}
