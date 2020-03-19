package com.example.carparkwhere.Utilities;

import androidx.annotation.NonNull;

import com.example.carparkwhere.Models.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CarparkReviewsDataManager {

    private NetworkCallEventListener networkCallEventListener;

    public void setNetworkCallEventListener(NetworkCallEventListener networkCallEventListener){
        this.networkCallEventListener = networkCallEventListener;
    }

    //this is working
    public static void addNewReview(final String carparkID, Double rating, final String review){
        Map<String,Object> reviewsMap = new HashMap<>();
        Map<String,ArrayList<Map<String,Object>> > reviews = new HashMap<>();

        final Map<String,Object> reviewMap = new HashMap<>();
        reviewMap.put("userId",FirebaseManager.getCurrentUser().getUid());
        reviewMap.put("rating",rating);
        reviewMap.put("review",review);
        reviewMap.put("displayName",UserDataManager.getDisplayName());
        reviewsMap.put("reviews", FieldValue.arrayUnion(reviewMap));

        ArrayList<Map<String,Object>> reviewList = new ArrayList<>();
        reviewList.add(reviewMap);
        reviews.put("reviews",reviewList);

        FirebaseManager.retrieveFromFirestore(FirebaseManager.CollectionsName.CARPARKREVIEWS.getString(), carparkID, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        System.out.println("exist");
                        FirebaseManager.updateFieldFirestoreArray(FirebaseManager.CollectionsName.CARPARKREVIEWS.getString(),carparkID,reviewMap);
                    }else{
                        FirebaseManager.insertToFirestore(FirebaseManager.CollectionsName.CARPARKREVIEWS.getString(),carparkID,review,null);
                    }

                }
            }
        });


       // FirebaseManager.insertToFirestore(FirebaseManager.CollectionsName.CARPARKREVIEWS.getString(),carparkID,reviews,null);
    }


    //this is working
//    public static void getReviewsOfCarpark(final String carparkID, final NetworkCallEventListener networkCallEventListener){
//        FirebaseManager.retrieveFromFirestore("carparkReviews", carparkID, new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()){
//                    ArrayList<Map<String,Object>> reviewMap = new ArrayList<>();
//                    ArrayList<Review> reviews = new ArrayList<>();
//                    reviewMap = (ArrayList<Map<String,Object>>) task.getResult().get("reviews");
//
//                    ArrayList<Review> reviewsList = new ArrayList<>();
//                    for (int x = 0 ; x < reviewMap.size() ; x++){
//                        Map<String,Object> eachMap = reviewMap.get(x);
//                        Double rating = (Double) eachMap.get("rating");
//                        String userComment = (String) eachMap.get("review");
//                        String displayName = (String) eachMap.get("displayName");
//
//                        Review review = new Review(null,(int)(Math.floor(rating)),carparkID,userComment);
//                        reviewsList.add(review);
//                    }
//                    networkCallEventListener.onComplete(reviewsList,true,null);
//                }else{
//                    networkCallEventListener.onComplete(null,false,task.getException().getMessage());
//                }
//            }
//        });
//    }

//    //only for reviews of the current registered signed in user
//    public static void editReviewOfCarpark(Review oldReview,Review newReview,String carparkID){
//        Map<String,Object> reviewsMap = new HashMap<>();
//        Map<String,Map<String,Object>> reviews = new HashMap<>();
//
//        Map<String,Object> reviewMap = new HashMap<>();
//        reviewMap.put("userId",FirebaseManager.getCurrentUser().getUid());
//        reviewMap.put("rating",oldReview.getRating());
//        reviewMap.put("review",oldReview.getComments());
//        reviewMap.put("displayName",UserDataManager.getDisplayName());
//        reviewsMap.put("reviews", FieldValue.arrayRemove(reviewMap));
//        FirebaseManager.updateFieldFirestoreArray(FirebaseManager.CollectionsName.CARPARKREVIEWS.getString(),carparkID,reviewsMap);
//
//        addNewReview(newReview.getCarPark().getCarparkName(),Double.valueOf(newReview.getRating()),newReview.getComments());
//    }


}
