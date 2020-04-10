package com.example.carparkwhere.Exceptions;

public class UserNotLoggedInException extends Exception {

    public UserNotLoggedInException(String errorMessage) {
        super(errorMessage);
    }

}
