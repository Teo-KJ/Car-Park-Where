package com.example.carparkwhere.DAO.DAOInterfaces;

import com.example.carparkwhere.Interfaces.NetworkCallEventListener;
import java.util.ArrayList;


/**
 * This is the data access object class for accessing bookmark information
 * @author kohsweesen
 */
public interface BookmarkDao {
    /**
     * This function helps to save the carpark bookmark information
     * @param carparkIds the carpark ids of the bookmarks
     * @param userEmail the email address of the user to be saved as bookmarks
     * @param networkCallEventListener the handler to handle the response of the network call
     */
    public void saveUserCarparkBookmark(ArrayList<String> carparkIds, String userEmail, final NetworkCallEventListener networkCallEventListener);

    /**
     * This function helps to access the carpark bookmark information
     * @param userEmail the email address of the specified user
     * @param networkCallEventListener the handler to handle the response of the network call
     */
    public void getUserBookmarkCarparkIds(String userEmail, final NetworkCallEventListener networkCallEventListener);
}
