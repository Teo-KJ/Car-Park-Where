package com.example.carparkwhere.DAO.DAOInterfaces;

import com.android.volley.Response;
import com.example.carparkwhere.Interfaces.NetworkCallEventListener;


/**
 * This is the data access object class for accessing carpark related information
 */
public interface CarparkDao {

    /**
     * This function makes a network call to the server to get server to fetch information in advanced so that future requests can be done quicker
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void getServerPrepared(final NetworkCallEventListener networkCallEventListener);

    /**
     * This function returns the details of the carpark, such as the price and location.
     * @param carparkID The carpark id of the carpark
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void getCarparkDetailsByID(String carparkID, final NetworkCallEventListener networkCallEventListener);

    /**
     * This function returns all the carparks coordinates available in Singapore
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void getAllCarparkCoordinates(final NetworkCallEventListener networkCallEventListener);

    /**
     * This function returns the full information of carpark which includes both static information and availability information
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void getAllCarparkEntireFullDetails(final NetworkCallEventListener networkCallEventListener);

    /**
     * This function returns the predicted availability of the whole day, which is used for generating the availability graph
     * @param carparkID the carpark id of the specified carpark
     * @param increment the increment is used to specify the day in the week. For example if today is Friday, increment=0 will mean Friday, increment=1 will mean Saturday, increment=3 will mean Monday
     * @param successListener the listener when the network request is successful
     * @param errorListener the listener when the network request is failure
     */
    public void getCarparkWholeDayPredictedAvailability(String carparkID, Integer increment, Response.Listener successListener, Response.ErrorListener errorListener);

    /**
     * This function returns the predicted availability at any specified given point of time
     * @param carparkID the carpark id of the specified carpark
     * @param year the year of the specified date
     * @param month the month of the specified date
     * @param day the day of the specified date
     * @param hour the hour of the specified date
     * @param minute the minute of the specified date
     * @param successListener the listener when the network request is successful
     * @param errorListener the listener when the network request is failure
     */
    public void getCarparkAvailabilityPredictionByDateTime(String carparkID,String year,String month, String day, String hour, String minute, Response.Listener successListener, Response.ErrorListener errorListener);

    /**
     * The function returns the live availability of the specified carpark.
     * @param carparkID the carpark id of the specified carpark
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void getCarparkLiveAvailability(String carparkID, final NetworkCallEventListener networkCallEventListener);


}
