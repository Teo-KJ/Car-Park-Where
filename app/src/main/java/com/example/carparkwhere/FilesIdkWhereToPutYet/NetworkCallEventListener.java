package com.example.carparkwhere.FilesIdkWhereToPutYet;

public interface NetworkCallEventListener {
    public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage);
}



