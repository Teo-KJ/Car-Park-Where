package com.example.carparkwhere.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.carparkwhere.FilesIdkWhereToPutYet.CarparkReviewsAdapter;
import com.example.carparkwhere.DAO.DAOImplementations.ReviewDaoImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.ReviewDao;
import com.example.carparkwhere.FilesIdkWhereToPutYet.NetworkCallEventListener;
import com.example.carparkwhere.ModelObjects.Review;
import com.example.carparkwhere.R;

import java.util.ArrayList;

public class CarparkReviewsActivity extends AppCompatActivity {

    private static final int DATASET_COUNT = 5;
    protected RecyclerView mRecyclerView;
    protected CarparkReviewsAdapter mAdapter;
    private ProgressDialog nDialog;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<Review> reviews = new ArrayList<Review>();
    private String carparkId;
    private ReviewDao reviewDaoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpark_reviews);

        reviewDaoHelper = new ReviewDaoImpl(this);

        presentProgressDialog("Loading Reviews");

        carparkId = getIntent().getStringExtra("carparkid");
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#111111")));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Reviews for Carpark " + carparkId);

        ImageButton backCarparkReviewsActivity_IMGBTN = findViewById(R.id.backCarparkReviewsActivity_IMGBTN);
        backCarparkReviewsActivity_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final RecyclerView reviews_RV = (RecyclerView) findViewById(R.id.reviews_RV);
        Review r1 = new Review("user001@email.com", 3.0,"001","Carpark was convenient to get to, but a bit too crowded on weekends.","user001",1);
        Review r2 = new Review("user007@email.com", 5.0,"001","This is my favorite carpark man.","user007",2);
        Review r3 = new Review("user008@email.com", 1.0,"001","Is this considered a carpark? I consider it a parked car.","user008",3);


        reviews.add(r1);
        reviews.add(r2);
        reviews.add(r3);


        //create adapter passing in the sample data
//        final CarparkReviewsAdapter adapter = new CarparkReviewsAdapter(reviews);
//
//        reviews_RV.setLayoutManager(new LinearLayoutManager(this));
//        reviews_RV.setAdapter(adapter);
//        Log.d(Integer.toString(adapter.getItemCount()),"ADAPTER COUNT");



        reviewDaoHelper.getCarparkReviewsByCarparkID(carparkId, new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                nDialog.dismiss();
                if (isSuccessful){
                    reviews = (ArrayList<Review>) networkCallResult;
                    final CarparkReviewsAdapter adapter = new CarparkReviewsAdapter(reviews);

                    for (Review review: reviews){
                        System.out.println(review.getDateString());
                    }
                    reviews_RV.setLayoutManager(new LinearLayoutManager(CarparkReviewsActivity.this));
                    reviews_RV.setAdapter(adapter);
                    Log.d(Integer.toString(adapter.getItemCount()),"ADAPTER COUNT");
                }
            }
        });

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

    private void presentProgressDialog(String message){
        nDialog = new ProgressDialog(this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle(message);
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }

}
