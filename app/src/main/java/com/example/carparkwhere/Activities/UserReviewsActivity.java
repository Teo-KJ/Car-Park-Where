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

        //Setting top layout
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#111111")));
        bar.setDisplayHomeAsUpEnabled(true);
        //Setting title
        bar.setTitle("Your Past Reviews");

        recyclerView = findViewById(R.id.recyclerView);
        // setting LinearLayout
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //this also can be done in XML
        // setting divider between views
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        //initialising data
        userReviews = new ArrayList<>();
        // getting user reviews using reviewDao by passing user's email
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
    //based on carpark id from review, information will be retrieved using carparkDao
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

