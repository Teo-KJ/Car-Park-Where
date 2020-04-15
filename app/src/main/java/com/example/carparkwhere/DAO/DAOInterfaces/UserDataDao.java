package com.example.carparkwhere.DAO.DAOInterfaces;

import android.content.Context;

import com.example.carparkwhere.Exceptions.UserNotLoggedInException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;


/**
 * This is the data access object class for accessing information related to user information
 */
public interface UserDataDao{

    /**
     * This function checks whether the user is currently logged in
     * @return Returns true if user is logged in, else false
     */
    public boolean isLoggedIn();

    /**
     * This function checks whether the current user is email verified
     * @return Returns true if user is email verified, else false
     */
    public boolean isEmailVerified();


    /**
     * This function helps to create new user
     * @param context the context to be used
     * @param email the email address of the new user
     * @param password the password of the new user
     * @param displayName the display name of the user user
     * @param handler the handler to handle the response of the network call
     */
    public void createNewUser(Context context, final String email, String password, final String displayName, OnCompleteListener<AuthResult> handler);

    /**
     * This function helps to sign in
     * @param context the context to be used
     * @param email the email address of the user
     * @param password the password of the user
     * @param handler the handler to handle the response of the network call
     */
    public void signInWithEmail(Context context, String email, String password, OnCompleteListener<AuthResult> handler);

    /**
     * This function helps to get the user email address of the currently login user
     * @return The string of user email address
     * @throws UserNotLoggedInException exception is thrown if the user is not login
     */
    public String getUserEmail() throws UserNotLoggedInException;

    /**
     * This function helps to get the current user display name
     * @return the display name of the user as a string
     * @throws UserNotLoggedInException exception is thrown if the user is not login
     */
    public String getDisplayName() throws UserNotLoggedInException;

    /**
     * This function helps to update the display name of the user
     * @param displayName the new display name of the user
     * @throws UserNotLoggedInException exception is thrown if the user is not login
     */
    public void updateDisplayName(String displayName) throws UserNotLoggedInException;

    /**
     * This function helps to sign out the user
     * @throws UserNotLoggedInException exception is thrown if the user is not login
     */
    public void signOut() throws UserNotLoggedInException;

    /**
     * This function helps to reset password by asking firebase to send a reset password email
     * @param email the email address which the reset password email to be sent to
     */
    public void sendResetPasswordEmail(String email);


}
