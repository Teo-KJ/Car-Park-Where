package com.example.carparkwhere.Utilities;

import java.lang.reflect.Type;

public interface NetworkCallEventListener {
    public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage);
}



