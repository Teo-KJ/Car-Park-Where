package com.example.carparkwhere.DAO.DAOImplementations;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carparkwhere.DAO.DAOInterfaces.CarparkDao;
import com.example.carparkwhere.Entities.Carpark;
import com.example.carparkwhere.Entities.Review;
import com.example.carparkwhere.R;
import com.example.carparkwhere.Interfaces.NetworkCallEventListener;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;


/**
 * This is the implementation class of the CarparkDao
 */

public class CarparkDaoImpl implements CarparkDao {

    /**
     * Request queue to be used for network request using the Volley library
     */
    private RequestQueue mQueue;

    /**
     * Context to be used for making network call
     */
    private Context context;


    /**
     * Initialiser for the class which is compulsory to pass in context as parameter
     * @param context is the context to be used for making network call request
     */
    public CarparkDaoImpl(Context context){
        this.context = context;
    }

    /**
     * This function makes a network call to the server to get server to fetch information in advanced so that future requests can be done quicker
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void getServerPrepared(final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = context.getResources().getString(R.string.serverUrl) + "/client/carparkdetails/prepare";
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                networkCallEventListener.onComplete("Prepared",true,null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                networkCallEventListener.onComplete(null,false,error.getMessage());
            }
        });
        mQueue.add(request);
    }

    //return type: Carpark
    /**
     * This function returns the details of the carpark, such as the price and location.
     * @param carparkID The carpark id of the carpark
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void getCarparkDetailsByID(String carparkID, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = context.getResources().getString(R.string.serverUrl) + "/client/carparkdetails/staticdetails/" + carparkID;
        // JsonObjectRequest request = new JsonObjectRequest(url, null, successListener,errorListener);
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                Carpark carpark = gson.fromJson(response.toString(), Carpark.class);
                networkCallEventListener.onComplete(carpark,true,null);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                networkCallEventListener.onComplete(null,false,error.getMessage());
            }
        });
        mQueue.add(request);
    }


    //return type: ArrayList<Carpark>

    /**
     * This function returns all the carparks coordinates available in Singapore
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void getAllCarparkCoordinates(final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = context.getResources().getString(R.string.serverUrl) + "/client/carparkdetails/brief";
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                ArrayList<Carpark> carparks = gson.fromJson(response.toString(),new TypeToken<ArrayList<Carpark>>(){}.getType());
                networkCallEventListener.onComplete(carparks,true,null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                networkCallEventListener.onComplete(new ArrayList<Carpark>(),false,error.getMessage());
            }
        });
        mQueue.add(request);
    }

    //Return type: ArrayList<Carpark>

    /**
     * This function returns the full information of carpark which includes both static information and availability information
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void getAllCarparkEntireFullDetails(final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = context.getResources().getString(R.string.serverUrl) + "/client/carparkdetails/";
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                ArrayList<Carpark> carparks = gson.fromJson(response.toString(),new TypeToken<ArrayList<Carpark>>(){}.getType());
                networkCallEventListener.onComplete(carparks,true,null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                networkCallEventListener.onComplete(new ArrayList<Carpark>(),false,error.getMessage());
            }
        });
        mQueue.add(request);
    }

    /**
     * This function returns the predicted availability of the whole day, which is used for generating the availability graph
     * @param carparkID the carpark id of the specified carpark
     * @param increment the increment is used to specify the day in the week. For example if today is Friday, increment=0 will mean Friday, increment=1 will mean Saturday, increment=3 will mean Monday
     * @param successListener the listener when the network request is successful
     * @param errorListener the listener when the network request is failure
     */
    public void getCarparkWholeDayPredictedAvailability(String carparkID, Integer increment, Response.Listener successListener, Response.ErrorListener errorListener){
        mQueue = Volley.newRequestQueue(context);
        String url = context.getResources().getString(R.string.serverUrl) + "/client/carparkdetails/prediction/" + carparkID + "/" + String.valueOf(increment);
        JsonArrayRequest request = new JsonArrayRequest(url, successListener, errorListener);
        mQueue.add(request);
    }

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
    public void getCarparkAvailabilityPredictionByDateTime(String carparkID,String year,String month, String day, String hour, String minute, Response.Listener successListener, Response.ErrorListener errorListener){
        mQueue = Volley.newRequestQueue(context);
        // 0 means for the current day itself only
        String url = context.getResources().getString(R.string.serverUrl) + "/client/carparkdetails/prediction/" + carparkID + "/0?date=" + year + "-" + ((month.length() == 1) ? "0"+month : month) + "-" + ((day.length() == 1) ? "0"+day : day) + "T" + ((hour.length() == 1) ? "0"+hour : hour) + ":" + ((minute.length() == 1) ? "0"+minute : minute);
        JsonObjectRequest request = new JsonObjectRequest(url, null, successListener,errorListener);
        mQueue.add(request);
    }


    /**
     * The function returns the live availability of the specified carpark.
     * @param carparkID the carpark id of the specified carpark
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void getCarparkLiveAvailability(String carparkID, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = context.getResources().getString(R.string.serverUrl) + "/client/carparkdetails/liveavailability/" + carparkID;
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    Integer liveAvailability = response.getInt("liveAvailability");
                    networkCallEventListener.onComplete(liveAvailability,true,null);
                }catch(Exception e){
                    e.printStackTrace();
                    networkCallEventListener.onComplete(null,false,"Json decoding failed");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                networkCallEventListener.onComplete(null,false,error.getMessage());
            }
        });
        mQueue.add(request);
    }


    //return type: ArrayList<Review>
    //note that empty array is still considered as success
    /**
     * This function returns all the reviews of the specified carparks
     * @param carparkID the carpark id of the specified carpark
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void getCarparkReviewsByCarparkID(String carparkID, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = context.getResources().getString(R.string.serverUrl)  + "/client/reviews/bycarpark/" + carparkID;
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();
                ArrayList<Review> reviews = gson.fromJson(response.toString(),new TypeToken<ArrayList<Review>>(){}.getType());
                networkCallEventListener.onComplete(reviews,true,null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                networkCallEventListener.onComplete(new ArrayList<Review>(),false,error.getMessage());
            }
        });
        mQueue.add(request);
    }


}
