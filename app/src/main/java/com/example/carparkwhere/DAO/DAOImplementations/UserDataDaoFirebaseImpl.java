package com.example.carparkwhere.DAO.DAOImplementations;

import com.example.carparkwhere.DAO.DAOInterfaces.UserDataDao;
import com.example.carparkwhere.FilesIdkWhereToPutYet.UserNotLoggedInException;
import com.example.carparkwhere.Utilities.FirebaseManager;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.HashMap;
import java.util.Map;

public class UserDataDaoFirebaseImpl implements UserDataDao {

    public boolean isLoggedIn(){
        return (FirebaseManager.getCurrentUser() != null);
    }

    public String getUserEmail() throws UserNotLoggedInException {
        if (isLoggedIn()){
            return FirebaseManager.getCurrentUser().getEmail();
        }else{
            throw new UserNotLoggedInException("User is not logged in");
        }
    }

    public String getDisplayName() throws UserNotLoggedInException{
        if (isLoggedIn()){
            return FirebaseManager.getCurrentUser().getDisplayName();
        }else{
            throw new UserNotLoggedInException("User is not logged in");
        }
    }


    public void updateDisplayName(String displayName) throws UserNotLoggedInException{
        if (isLoggedIn()){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(displayName).build();
            FirebaseManager.getCurrentUser().updateProfile(profileUpdates);
        }else{
            throw new UserNotLoggedInException("User is not logged in");
        }

    }


}
