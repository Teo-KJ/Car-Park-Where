package com.example.carparkwhere.Interfaces;


/**
 * This is the interface to manage network call result
 * @author kohsweesen
 */
public interface NetworkCallEventListener {
    public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage);
}



