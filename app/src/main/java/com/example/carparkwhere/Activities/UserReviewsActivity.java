package com.example.carparkwhere.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.carparkwhere.DAO.DAOImplementations.CarparkDaoImpl;
import com.example.carparkwhere.DAO.DAOImplementations.ReviewDaoImpl;
import com.example.carparkwhere.DAO.DAOImplementations.UserDataDaoFirebaseImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.CarparkDao;
import com.example.carparkwhere.DAO.DAOInterfaces.ReviewDao;
import com.example.carparkwhere.DAO.DAOInterfaces.UserDataDao;
import com.example.carparkwhere.Interfaces.NetworkCallEventListener;
import com.example.carparkwhere.Exceptions.UserNotLoggedInException;
import com.example.carparkwhere.Adaptors.UserReviewAdapter;
import com.example.carparkwhere.Entities.Carpark;
import com.example.carparkwhere.Entities.Review;
import com.example.carparkwhere.R;

import java.util.ArrayList;
import java.util.List;

public class UserReviewsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    UserReviewAdapter recyclerAdapter;
    List<Review> userReviews;
    ReviewDao reviewDaoHelper;
    UserDataDao userDataDaoHelper;
    CarparkDao carparkDaoHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reviews);

        reviewDaoHelper = new ReviewDaoImpl(this);
        userDataDaoHelper = new UserDataDaoFirebaseImpl();
        carparkDaoHelper = new CarparkDaoImpl(this);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#111111")));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Your Past Reviews");

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
//        Review first = new Review("jordon@email.com", 5.0,"NTU Northspine", "Brightly lit and clean", "Jordon",12);
//        Review second = new Review("jordon@email.com", 4.0,"NIE", "Clean, little lots", "Jordon",12);
//        Review third = new Review("jordon@email.com", 1.0,"Hall 7", "Bad, no food, nothing nearby", "Jordon",12);
//        userReviews.add(first);
//        userReviews.add(second);
//        userReviews.add(third);

        try{
            reviewDaoHelper.getCarparkReviewsByUserEmail(userDataDaoHelper.getUserEmail(), new NetworkCallEventListener() {
                @Override
                public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                    if (isSuccessful){
                        userReviews = (ArrayList<Review>) networkCallResult;
                        getCarparkName();
                        initRecyclerView();
                    }
                }
            });
        }catch (UserNotLoggedInException e){
            e.printStackTrace();
        }


    }

    private void getCarparkName(){
        for (Review review:userReviews){
            carparkDaoHelper.getCarparkDetailsByID(review.getCarparkId(), new NetworkCallEventListener() {
                @Override
                public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                    if (isSuccessful){
                        Carpark carpark = (Carpark) networkCallResult;
                        review.setCarparkName(carpark.getCarparkName());
                        recyclerAdapter.notifyDataSetChanged();
                    }
                }
            });
        }

    }



    private void initRecyclerView() {
        recyclerAdapter = new UserReviewAdapter(userReviews);
        recyclerView.setAdapter(recyclerAdapter);
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

    @Override
    protected void onResume() {
        super.onResume();
        try{
            reviewDaoHelper.getCarparkReviewsByUserEmail(userDataDaoHelper.getUserEmail(), new NetworkCallEventListener() {
                @Override
                public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                    if (isSuccessful){
                        userReviews = (ArrayList<Review>) networkCallResult;
                        getCarparkName();
                        initRecyclerView();
                    }
                }
            });
        }catch (UserNotLoggedInException e){
            e.printStackTrace();
        }
    }
}

