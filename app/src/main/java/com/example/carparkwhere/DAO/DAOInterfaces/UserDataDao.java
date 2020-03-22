package com.example.carparkwhere.DAO.DAOInterfaces;

import com.example.carparkwhere.FilesIdkWhereToPutYet.UserNotLoggedInException;


public interface UserDataDao{

    public boolean isLoggedIn();
    public String getUserEmail() throws UserNotLoggedInException;
    public String getDisplayName() throws UserNotLoggedInException;
    public void updateDisplayName(String displayName) throws UserNotLoggedInException;

}
