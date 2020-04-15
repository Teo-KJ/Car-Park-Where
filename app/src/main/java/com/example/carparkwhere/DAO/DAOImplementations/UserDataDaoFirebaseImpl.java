package com.example.carparkwhere.DAO.DAOImplementations;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.carparkwhere.DAO.DAOInterfaces.UserDataDao;
import com.example.carparkwhere.Exceptions.UserNotLoggedInException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;


/**
 * This is the implementation class for UserDataDao. This is a firebase implementation
 */
public class UserDataDaoFirebaseImpl implements UserDataDao {

    /**
     * Firebase authentication instance
     */
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     * This function checks whether the user is currently logged in
     * @return Returns true if user is logged in, else false
     */
    public boolean isLoggedIn(){
        return (mAuth.getCurrentUser() != null);
    }

    /**
     * This function checks whether the current user is email verified
     * @return Returns true if user is email verified, else false
     */
    public boolean isEmailVerified(){
        return (mAuth.getCurrentUser().isEmailVerified());
    }

    /**
     * This function helps to get the user email address of the currently login user
     * @return The string of user email address
     * @throws UserNotLoggedInException exception is thrown if the user is not login
     */
    public String getUserEmail() throws UserNotLoggedInException {
        if (isLoggedIn()){
            return mAuth.getCurrentUser().getEmail();
        }else{
            throw new UserNotLoggedInException("User is not logged in");
        }
    }


    /**
     * This function helps to get the current user display name
     * @return the display name of the user as a string
     * @throws UserNotLoggedInException exception is thrown if the user is not login
     */
    public String getDisplayName() throws UserNotLoggedInException{
        if (isLoggedIn()){
            return mAuth.getCurrentUser().getDisplayName();
        }else{
            throw new UserNotLoggedInException("User is not logged in");
        }
    }

    /**
     * This function helps to update the display name of the user
     * @param displayName the new display name of the user
     * @throws UserNotLoggedInException exception is thrown if the user is not login
     */
    public void updateDisplayName(String displayName) throws UserNotLoggedInException{
        if (isLoggedIn()){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();
            mAuth.getCurrentUser().updateProfile(profileUpdates);
        }else{
            throw new UserNotLoggedInException("User is not logged in");
        }

    }

    /**
     * This function helps to sign out the user
     * @throws UserNotLoggedInException exception is thrown if the user is not login
     */
    public void signOut() throws UserNotLoggedInException{
        if (isLoggedIn()){
            mAuth.signOut();
        }else{
            throw new UserNotLoggedInException("User is not logged in");
        }
    }

    /**
     * This function helps to reset password by asking firebase to send a reset password email
     * @param email the email address which the reset password email to be sent to
     */
    public void sendResetPasswordEmail(String email){
        mAuth.sendPasswordResetEmail(email);
    }

    /**
     * This function helps to sign in
     * @param context the context to be used
     * @param email the email address of the user
     * @param password the password of the user
     * @param handler the handler to handle the response of the network call
     */
    public void signInWithEmail(Context context, String email, String password, OnCompleteListener<AuthResult> handler){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context,handler);
    }


    /**
     * This function helps to create new user
     * @param context the context to be used
     * @param email the email address of the new user
     * @param password the password of the new user
     * @param displayName the display name of the user user
     * @param handler the handler to handle the response of the network call
     */
    public void createNewUser(Context context, final String email, String password, final String displayName, OnCompleteListener<AuthResult> handler){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, handler)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                });
    }


}
