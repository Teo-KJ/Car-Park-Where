package com.example.carparkwhere.Models;

import java.util.List;

public class Review {

    private String userDisplayName;
    private Double userRating;
    private String carparkId;
    private String userComments;

    public Review(String userDisplayName, Double userRating, String carparkId, String userComments){
        this.userDisplayName = userDisplayName;
        this.userRating = userRating;
        this.carparkId = carparkId;
        this.userComments = userComments;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public Double getUserRating() {
        return userRating;
    }

    public String getCarparkId() {
        return carparkId;
    }

    public String getUserComments() {
        return userComments;
    }


    public void setCarPark(String carparkID){
        //this.carpark = ...?

    }

    public void setUser(String userEmail){

    }

    public void setRating(Double rating){
        this.userRating = rating;
    }

}
