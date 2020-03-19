package com.example.carparkwhere.Models;

import java.util.List;

public class Review {

    private String userEmail;
    private Double userRating;
    private String carparkId;
    private String userDisplayName;
    private String userComment;
    private String _id;

    public Review(String userEmail, Double userRating, String carparkId, String userComment, String userDisplayName){
        this.userEmail = userEmail;
        this.userRating = userRating;
        this.carparkId = carparkId;
        this.userComment = userComment;
        this.userDisplayName = userDisplayName;
        this._id = null;

    }

    //getters setters
    public String getCarparkId(){
        return this.carparkId;
    }
    public String getUserEmail(){
        return this.userEmail;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }
    public Double getRating(){
        return this.userRating;
    }
    public String getComment(){
        return this.userComment;
    }

    public String get_id() {
        return _id;
    }
    //    public void setCarPark(Carpark carpark){
//        this.carpark = carpark;
//    }
//    public void setUser(RegisteredUser user){
//        this.user = user;
//    }
//    public void setRating(int rating){
//        this.userRating = rating;
//    }
//    public void setComments(String comments){
//        this.userComments = comments;
//    }

}
