package com.example.carparkwhere.DAO.DAOInterfaces;

import com.example.carparkwhere.Interfaces.NetworkCallEventListener;
import java.util.ArrayList;


public interface BookmarkDao {
    public void saveUserCarparkBookmark(ArrayList<String> carparkIds, String userEmail, final NetworkCallEventListener networkCallEventListener);
    public void getUserBookmarkCarparkIds(String userEmail, final NetworkCallEventListener networkCallEventListener);
}
