package com.example.carparkwhere.DAO.DAOInterfaces;

import android.content.Context;

import com.example.carparkwhere.FilesIdkWhereToPutYet.UserNotLoggedInException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;


public interface UserDataDao{

    public boolean isLoggedIn();
    public boolean isEmailVerified();
    public void createNewUser(Context context, final String email, String password, final String displayName, OnCompleteListener<AuthResult> handler);
    public void signInWithEmail(Context context, String email, String password, OnCompleteListener<AuthResult> handler);
    public String getUserEmail() throws UserNotLoggedInException;
    public String getDisplayName() throws UserNotLoggedInException;
    public void updateDisplayName(String displayName) throws UserNotLoggedInException;
    public void signOut() throws UserNotLoggedInException;
    public void sendResetPasswordEmail(String email);


}
