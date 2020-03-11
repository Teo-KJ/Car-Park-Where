package com.example.carparkwhere.Models;

import java.util.ArrayList;
import java.util.List;

public class RegisteredUser {
    private String name;
    public String email;
    public Boolean isValidated;
    public String password;

    public RegisteredUser(String name, String email, Boolean isValidated, String password) {
        this.name = name;
        this.email = email;
        this.isValidated = isValidated;
        this.password = password;
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

    public void setPassword(String password){
        this.password = password;
    }

    public ArrayList<Review> getUserReviews(){
        ArrayList<Review> reviewList = new ArrayList<Review>();
        return reviewList;
    }

}
