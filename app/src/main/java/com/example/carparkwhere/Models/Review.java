package com.example.carparkwhere.Models;

import java.util.List;

public class Review {

    private RegisteredUser user;
    public int userRating;
    private Carpark carpark;
    private String userComments;

    public Review(RegisteredUser user, int userRating, Carpark carpark, String userComments){
        this.user = user;
        this.userRating = userRating;
        this.carpark = carpark;
        this.userComments = userComments;

    }

    public void loadReviewData(){

    }

    public void saveReviewData(){

    }

    public List<Review> getCarparkReviews(){
        List<Review> reviewList = null;

        return reviewList;
    }

    public void setCarPark(String carparkID){
        //this.carpark = ...?

    }

    public void setUser(String userEmail){

    }

    public void setRating(int rating){
        this.userRating = rating;
    }

}
