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


public class UserDataDaoFirebaseImpl implements UserDataDao {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public boolean isLoggedIn(){
        return (mAuth.getCurrentUser() != null);
    }

    public boolean isEmailVerified(){
        return (mAuth.getCurrentUser().isEmailVerified());
    }

    public String getUserEmail() throws UserNotLoggedInException {
        if (isLoggedIn()){
            return mAuth.getCurrentUser().getEmail();
        }else{
            throw new UserNotLoggedInException("User is not logged in");
        }
    }

    public String getDisplayName() throws UserNotLoggedInException{
        if (isLoggedIn()){
            return mAuth.getCurrentUser().getDisplayName();
        }else{
            throw new UserNotLoggedInException("User is not logged in");
        }
    }


    public void updateDisplayName(String displayName) throws UserNotLoggedInException{
        if (isLoggedIn()){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();
            mAuth.getCurrentUser().updateProfile(profileUpdates);
        }else{
            throw new UserNotLoggedInException("User is not logged in");
        }

    }

    public void signOut() throws UserNotLoggedInException{
        if (isLoggedIn()){
            mAuth.signOut();
        }else{
            throw new UserNotLoggedInException("User is not logged in");
        }
    }

    public void sendResetPasswordEmail(String email){
        mAuth.sendPasswordResetEmail(email);
    }

    public void signInWithEmail(Context context, String email, String password, OnCompleteListener<AuthResult> handler){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context,handler);
    }

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
