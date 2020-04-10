package com.example.carparkwhere.Entities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Review {

    private String userEmail;
    private Double userRating;
    private String carparkId;
    private String carparkName;
    private String userDisplayName;
    private String userComment;
    private String _id;
    private Integer date;

    public Review(String userEmail, Double userRating, String carparkId, String userComment, String userDisplayName,Integer date){
        this.userEmail = userEmail;
        this.userRating = userRating;
        this.carparkId = carparkId;
        this.userComment = userComment;
        this.userDisplayName = userDisplayName;
        this._id = null;
        this.date = date;
        this.carparkName = "";
    }

    //getters setters
    public String getCarparkId(){
        return this.carparkId;
    }
    public String getUserEmail(){
        return this.userEmail;
    }

    public String getCarparkName() {
        return carparkName;
    }

    public void setCarparkName(String carparkName) {
        this.carparkName = carparkName;
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

    public String getDateString() {
        long unixSeconds = this.date.longValue();
        Date date = new java.util.Date(unixSeconds*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy • HH:mm");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }

    public Integer getDate() {
        return date;
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
