package com.example.carparkwhere.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.carparkwhere.Adaptors.CarparkReviewsAdapter;
import com.example.carparkwhere.DAO.DAOImplementations.ReviewDaoImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.ReviewDao;
import com.example.carparkwhere.Interfaces.NetworkCallEventListener;
import com.example.carparkwhere.Entities.Review;
import com.example.carparkwhere.R;

import java.util.ArrayList;

public class CarparkReviewsActivity extends AppCompatActivity {

    private static final int DATASET_COUNT = 5;
    protected RecyclerView mRecyclerView;
    protected CarparkReviewsAdapter mAdapter;
    private ProgressBar progressBar1,progressBar2,progressBar3,progressBar4,progressBar5;
    private TextView totalReviewCountText,overallReviewAverage_TV;
    private ProgressDialog nDialog;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<Review> reviews = new ArrayList<Review>();
    private String carparkId;
    private ReviewDao reviewDaoHelper;
    private RecyclerView reviews_RV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpark_reviews);

        reviewDaoHelper = new ReviewDaoImpl(this);
        progressBar1 = findViewById(R.id.progress_bar_star1);
        progressBar2 = findViewById(R.id.progress_bar_star2);
        progressBar3 = findViewById(R.id.progress_bar_star3);
        progressBar4 = findViewById(R.id.progress_bar_star4);
        progressBar5 = findViewById(R.id.progress_bar_star5);
        totalReviewCountText = findViewById(R.id.totalReviewCountText);
        overallReviewAverage_TV = findViewById(R.id.overallReviewAverage_TV);

        presentProgressDialog("Loading Reviews");

        carparkId = getIntent().getStringExtra("carparkid");
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#111111")));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.openOptionsMenu();
        bar.setTitle("Reviews for Carpark " + carparkId);

        reviews_RV = (RecyclerView) findViewById(R.id.reviews_RV);


        reviewDaoHelper.getCarparkAverageRating(carparkId, new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                if (isSuccessful){
                    Double average = (Double) networkCallResult;
                    Double rounded = Math.round(average.doubleValue() * 10) / 10.0;
                    overallReviewAverage_TV.setText(rounded.toString());
                }
            }
        });

        reviewDaoHelper.getCarparkReviewsByCarparkID(carparkId, new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                nDialog.dismiss();
                if (isSuccessful) {
                    reviews = (ArrayList<Review>) networkCallResult;
                    setupRecyclerList();
                }
            }
        });

    }

    private void setupRecyclerList(){
        mAdapter = new CarparkReviewsAdapter(reviews);

        reviews_RV.setLayoutManager(new LinearLayoutManager(CarparkReviewsActivity.this));
        reviews_RV.setAdapter(mAdapter);

        Integer maxnum = 0;
        Integer star1Num = 0;
        Integer star2Num = 0;
        Integer star3Num = 0;
        Integer star4Num = 0;
        Integer star5Num = 0;

        for (Review review: reviews){
            int reviewRating = (int) Math.floor(review.getRating());
            if (reviewRating == 1){
                star1Num += 1;
                maxnum = Math.max(maxnum,star1Num);
            }else if (reviewRating == 2){
                star2Num += 1;
                maxnum = Math.max(maxnum,star2Num);
            }else if (reviewRating == 3){
                star3Num += 1;
                maxnum = Math.max(maxnum,star3Num);
            }else if (reviewRating == 4){
                star4Num += 1;
                maxnum = Math.max(maxnum,star4Num);
            }else if (reviewRating == 5){
                star5Num += 1;
                maxnum = Math.max(maxnum,star5Num);
            }
        }

        if (reviews.size() != 0){
            progressBar1.setProgress(Math.round((star1Num.floatValue() / maxnum.floatValue()) * 100));
            progressBar2.setProgress(Math.round((star2Num.floatValue() / maxnum.floatValue()) * 100));
            progressBar3.setProgress(Math.round((star3Num.floatValue() / maxnum.floatValue()) * 100));
            progressBar4.setProgress(Math.round((star4Num.floatValue() / maxnum.floatValue()) * 100));
            progressBar5.setProgress(Math.round((star5Num.floatValue() / maxnum.floatValue()) * 100));

            totalReviewCountText.setText("(" + (star1Num + star2Num + star3Num + star4Num + star5Num) + ")");
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

        }

        return super.onOptionsItemSelected(item);
    }

    private void presentProgressDialog(String message){
        nDialog = new ProgressDialog(this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle(message);
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reviewDaoHelper.getCarparkReviewsByCarparkID(carparkId, new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                if (isSuccessful){
                    reviews = (ArrayList<Review>) networkCallResult;
                    setupRecyclerList();
                }
            }
        });
    }

}
