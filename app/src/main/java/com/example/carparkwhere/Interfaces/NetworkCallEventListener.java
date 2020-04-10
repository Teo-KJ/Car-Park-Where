package com.example.carparkwhere.Interfaces;

public interface NetworkCallEventListener {
    public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage);
}



