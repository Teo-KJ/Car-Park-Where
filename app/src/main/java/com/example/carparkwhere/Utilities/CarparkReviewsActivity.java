package com.example.carparkwhere.Utilities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.carparkwhere.CarparkReviewsAdapter;
import com.example.carparkwhere.Models.Review;
import com.example.carparkwhere.R;

import java.util.ArrayList;

public class CarparkReviewsActivity extends AppCompatActivity {
    private static final int DATASET_COUNT = 5;
    protected RecyclerView mRecyclerView;
    protected CarparkReviewsAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected ArrayList<Review> reviews = new ArrayList<Review>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpark_reviews);

        ImageButton backCarparkReviewsActivity_IMGBTN = findViewById(R.id.backCarparkReviewsActivity_IMGBTN);
        backCarparkReviewsActivity_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView reviews_RV = (RecyclerView) findViewById(R.id.reviews_RV);
        Review r1 = new Review("user001@email.com", 3.0,"001","Carpark was convenient to get to, but a bit too crowded on weekends.","user001");
        Review r2 = new Review("user007@email.com", 5.0,"001","This is my favorite carpark man.","user007");
        Review r3 = new Review("user008@email.com", 1.0,"001","Is this considered a carpark? I consider it a parked car.","user008");


        reviews.add(r1);
        reviews.add(r2);
        reviews.add(r3);

        //create adapter passing in the sample data
        CarparkReviewsAdapter adapter = new CarparkReviewsAdapter(reviews);

        reviews_RV.setLayoutManager(new LinearLayoutManager(this));
        reviews_RV.setAdapter(adapter);
        Log.d(Integer.toString(adapter.getItemCount()),"ADAPTER COUNT");

    }


}
