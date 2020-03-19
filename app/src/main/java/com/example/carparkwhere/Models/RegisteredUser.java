package com.example.carparkwhere.Models;

import java.util.ArrayList;
import java.util.List;

public class RegisteredUser {
    private String name;
    private String email;
    private Boolean isValidated;
    private String password;

    public RegisteredUser(String name, String email, Boolean isValidated) {
        this.name = name;
        this.email = email;
        this.isValidated = isValidated;
    }

    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }

    public boolean getIsValidated(){
        return this.isValidated;
    }

    public String getPassword(){
        return this.password;
    }

    public void setName(String name){
        this.name = name;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public void setIsValidated(boolean isValidated){
        this.isValidated = isValidated;
    }

    public void setPassword(String password){
        this.password = password;
    }


}
