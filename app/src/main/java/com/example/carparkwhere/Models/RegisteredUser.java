package com.example.carparkwhere.Models;

import java.util.ArrayList;
import java.util.List;

public class RegisteredUser {
    private String name;
    public String email;
    public Boolean isValidated;

    public RegisteredUser(String name, String email, Boolean isValidated) {
        this.name = name;
        this.email = email;
        this.isValidated = isValidated;
    }

    public void loadUserData(){

    }

    public void saveUserData(){

    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }


    public ArrayList<Review> getUserReviews(){
        ArrayList<Review> reviewList = new ArrayList<Review>();
        return reviewList;
    }

}
