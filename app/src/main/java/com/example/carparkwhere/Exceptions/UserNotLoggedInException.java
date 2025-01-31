package com.example.carparkwhere.Exceptions;


/**
 * This is the exception class to handle exception thrown if user is not logged in
 * @author kohsweesen
 */
public class UserNotLoggedInException extends Exception {

    public UserNotLoggedInException(String errorMessage) {
        super(errorMessage);
    }

}
