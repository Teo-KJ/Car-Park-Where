package com.example.carparkwhere.Entities;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * This class implements the Review entity with the attributes userEmail, userRating, carparkId, carparkName, userDisplayName, userComment, _id and date.
 *
 * @author Tay Jaslyn
 * */
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


}
