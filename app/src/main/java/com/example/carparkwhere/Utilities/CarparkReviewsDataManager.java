package com.example.carparkwhere.Utilities;

import androidx.annotation.NonNull;

import com.example.carparkwhere.Models.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CarparkReviewsDataManager {

    private NetworkCallEventListener networkCallEventListener;

    public void setNetworkCallEventListener(NetworkCallEventListener networkCallEventListener){
        this.networkCallEventListener = networkCallEventListener;
    }

    public static void addNewReview(String carparkID, Double rating,String review){

        Map<String,Object> reviewsMap = new HashMap<>();
        ArrayList<Map<String,Object>> reviews = new ArrayList<>();
        Map<String,Object> reviewMap = new HashMap<>();
        reviewMap.put("userId",FirebaseManager.getCurrentUser().getUid());
        reviewMap.put("rating",rating);
        reviewMap.put("review",review);
        reviews.add(reviewMap);
        reviewsMap.put("reviews",reviews);

        FirebaseManager.updateFieldFirestore(FirebaseManager.CollectionsName.CARPARKREVIEWS.getString(),carparkID,reviewsMap);

    }

    //    Map<String,Object> carparkMap = new HashMap<>();
//    ArrayList<String> carparks = new ArrayList<>();
//    carparks = (ArrayList<String>) task.getResult().get("carparks");

    public static void getReviewsOfCarpark(String carparkID, final NetworkCallEventListener networkCallEventListener){
        FirebaseManager.retrieveFromFirestore("carparkReviews", carparkID, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    ArrayList<Map<String,Object>> reviewMap = new ArrayList<>();
                    ArrayList<Review> reviews = new ArrayList<>();
                    reviewMap = (ArrayList<Map<String,Object>>) task.getResult().get("reviews");

                    ArrayList<Review> reviewsList = new ArrayList<>();
                    for (int x = 0 ; x < reviewMap.size() ; x++){
                        Map<String,Object> eachMap = reviewMap.get(x);
                        int rating = (int) eachMap.get("rating");

                        //to be continued
                    }

                    System.out.println(reviewMap);
                    networkCallEventListener.onComplete(reviews,true,null);
                }else{
                    networkCallEventListener.onComplete(null,false,task.getException().getMessage());
                }
            }
        });
    }
}
