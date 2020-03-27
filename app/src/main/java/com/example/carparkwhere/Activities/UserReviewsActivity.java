package com.example.carparkwhere.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.carparkwhere.FilesIdkWhereToPutYet.UserReviewAdapter;
import com.example.carparkwhere.ModelObjects.Review;
import com.example.carparkwhere.R;

import java.util.ArrayList;
import java.util.List;

public class UserReviewsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    UserReviewAdapter recyclerAdapter;
    List<Review> userReviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reviews);

        recyclerView = findViewById(R.id.recyclerView);
        // setting LinearLayout
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //this also can be done in XML
        //Making sure divider nice nice
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        //initialising data
        // I changed userRating to a float
        userReviews = new ArrayList<>();
        //can retrieve from firebase here
//        Review first = new Review("jordon@email.com", (float) 5.0,"NTU Northspine", "Brightly lit and clean", "Jordon");
//        Review second = new Review("jordon@email.com", (float) 4.0,"NIE", "Clean, little lots", "Jordon");
//        Review third = new Review("jordon@email.com", (float) 1.0,"Hall 7", "Bad, no food, nothing nearby", "Jordon");
//        userReviews.add(first);
//        userReviews.add(second);
//        userReviews.add(third);
        initRecyclerView();
    }



    private void initRecyclerView() {
        recyclerAdapter = new UserReviewAdapter(userReviews);
        recyclerView.setAdapter(recyclerAdapter);
    }



}

