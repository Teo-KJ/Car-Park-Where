package com.example.carparkwhere.Models;

import java.util.List;

public class Review {

    private RegisteredUser user;
    private int userRating;
    private Carpark carpark;
    private String userComments;

    public Review(RegisteredUser user, int userRating, Carpark carpark, String userComments){
        this.user = user;
        this.userRating = userRating;
        this.carpark = carpark;
        this.userComments = userComments;

    }

    //getters setters
    public Carpark getCarPark(){
        return this.carpark;
    }
    public RegisteredUser getUser(){
        return this.user;
    }

    public int getRating(){
        return this.userRating;
    }

    public String getComments(){
        return this.userComments;
    }

    public void setCarPark(Carpark carpark){
        this.carpark = carpark;
    }
    public void setUser(RegisteredUser user){
        this.user = user;
    }
    public void setRating(int rating){
        this.userRating = rating;
    }
    public void setComments(String comments){
        this.userComments = comments;
    }

}
