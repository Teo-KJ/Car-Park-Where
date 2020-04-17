package com.example.carparkwhere.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carparkwhere.DAO.DAOImplementations.CarparkDaoImpl;
import com.example.carparkwhere.DAO.DAOImplementations.ReviewDaoImpl;
import com.example.carparkwhere.DAO.DAOImplementations.UserDataDaoFirebaseImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.CarparkDao;
import com.example.carparkwhere.DAO.DAOInterfaces.ReviewDao;
import com.example.carparkwhere.DAO.DAOInterfaces.UserDataDao;
import com.example.carparkwhere.Exceptions.UserNotLoggedInException;
import com.example.carparkwhere.Entities.Carpark;
import com.example.carparkwhere.Entities.Review;
import com.example.carparkwhere.Interfaces.NetworkCallEventListener;
import com.example.carparkwhere.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/*
* This class implements the SubmitReview Activity. This is used to handle the interactions of the user with the user interface.
*
* @author Tay Jaslyn, Koh Swee Sen
* */


public class SubmitReviewActivity extends AppCompatActivity {

    RatingBar reviewRating_RBAR;
    TextView reviewComment_ET;
    TextView reviewLocation_TV;
    ImageButton reviewBack_IMGBTN;
    Button reviewDelete_BTN;
    ProgressDialog nDialog;
    private String carparkId = "";
    private Boolean isEditingReview = false;
    private Review oldReview = null;

    private ReviewDao reviewDaoHelper;
    private CarparkDao carparkDaoHelper;
    private UserDataDao userDataDaoHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_review);

        reviewDaoHelper = new ReviewDaoImpl(this);
        carparkDaoHelper = new CarparkDaoImpl(this);
        userDataDaoHelper = new UserDataDaoFirebaseImpl();

        reviewRating_RBAR = findViewById(R.id.reviewRating_RBAR);
        reviewComment_ET = findViewById(R.id.reviewComment_ET);
        reviewLocation_TV = findViewById(R.id.reviewLocation_TV);
        carparkId = getIntent().getStringExtra("carparkid");


        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#111111")));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Submit Review for Carpark " + carparkId);


        carparkDaoHelper.getCarparkDetailsByID(carparkId, new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                if (isSuccessful){
                    Carpark carpark = (Carpark) networkCallResult;
                    reviewLocation_TV.setText(carpark.getCarparkName());
                }
            }
        });


        //set back button
        reviewBack_IMGBTN = findViewById(R.id.reviewBack_IMGBTN);
        reviewBack_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //save review
        final Button reviewSave_BTN = findViewById(R.id.reviewSave_BTN);
        reviewSave_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processReviewSubmission();
            }
        });

        //delete review
        reviewDelete_BTN = findViewById(R.id.reviewDelete_BTN);
        reviewDelete_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processReviewDeletion();
            }
        });
        reviewDelete_BTN.setVisibility(View.INVISIBLE);


        try{
            reviewDaoHelper.getCarparkReviewsByUserEmail(userDataDaoHelper.getUserEmail(), new NetworkCallEventListener() {
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
        }catch (UserNotLoggedInException e){

        }






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

        reviewDaoHelper.deleteCarparkReviewByReviewID(oldReview.get_id(), new NetworkCallEventListener() {
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
            try{
                Calendar cal = Calendar.getInstance();
                TimeZone timeZone = cal.getTimeZone();
                Date cals = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).getTime();
                long milliseconds = cals.getTime();
                milliseconds = milliseconds + TimeZone.getTimeZone("GMT+8").getOffset(milliseconds);
                long unixTimeStamp = milliseconds / 1000L;
                Integer unixTime = (int) unixTimeStamp;

                Review newReview = new Review(userDataDaoHelper.getUserEmail(),Double.valueOf(reviewRating_RBAR.getRating()),carparkId,reviewComment_ET.getText().toString(),userDataDaoHelper.getDisplayName(),unixTime);
                reviewDaoHelper.updateCarparkReviewWithNewValues(oldReview.get_id(), newReview, new NetworkCallEventListener() {
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
            }catch (UserNotLoggedInException e){

            }

        }

        else{
            if (reviewRating_RBAR.getRating() != 0 && !reviewComment_ET.getText().toString().isEmpty()){
                try{
                    Calendar cal = Calendar.getInstance();
                    TimeZone timeZone = cal.getTimeZone();
                    Date cals = Calendar.getInstance(TimeZone.getDefault()).getTime();
                    long milliseconds = cals.getTime();
                    milliseconds = milliseconds + timeZone.getOffset(milliseconds);
                    long unixTimeStamp = milliseconds / 1000L;
                    Integer unixTime = (int) unixTimeStamp;

                    Review newReview = new Review(userDataDaoHelper.getUserEmail(),Double.valueOf(reviewRating_RBAR.getRating()),carparkId,reviewComment_ET.getText().toString(),userDataDaoHelper.getDisplayName(),unixTime);
                    reviewDaoHelper.saveNewCarparkReview(newReview, new NetworkCallEventListener() {
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
                }catch (UserNotLoggedInException e){

                }

            }
        }


    }

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
