package com.example.carparkwhere.DAO.DAOInterfaces;

import com.android.volley.Response;
import com.example.carparkwhere.Interfaces.NetworkCallEventListener;

public interface CarparkDao {


    public void getServerPrepared(final NetworkCallEventListener networkCallEventListener);
    public void getCarparkDetailsByID(String carparkID, final NetworkCallEventListener networkCallEventListener);
    public void getAllCarparkCoordinates(final NetworkCallEventListener networkCallEventListener);
    public void getAllCarparkEntireFullDetails(final NetworkCallEventListener networkCallEventListener);
    public void getCarparkWholeDayPredictedAvailability(String carparkID, Integer increment, Response.Listener successListener, Response.ErrorListener errorListener);
    public void getCarparkAvailabilityPredictionByDateTime(String carparkID,String year,String month, String day, String hour, String minute, Response.Listener successListener, Response.ErrorListener errorListener);
    public void getCarparkLiveAvailability(String carparkID, final NetworkCallEventListener networkCallEventListener);


}
