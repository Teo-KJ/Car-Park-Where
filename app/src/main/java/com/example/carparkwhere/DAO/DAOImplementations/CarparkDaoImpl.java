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

public class CarparkDaoImpl implements CarparkDao {

    private RequestQueue mQueue;
    private Context context;

    public CarparkDaoImpl(Context context){
        this.context = context;
    }

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

    public void getCarparkWholeDayPredictedAvailability(String carparkID, Integer increment, Response.Listener successListener, Response.ErrorListener errorListener){
        mQueue = Volley.newRequestQueue(context);
        String url = context.getResources().getString(R.string.serverUrl) + "/client/carparkdetails/prediction/" + carparkID + "/" + String.valueOf(increment);
        JsonArrayRequest request = new JsonArrayRequest(url, successListener, errorListener);
        mQueue.add(request);
    }

    public void getCarparkAvailabilityPredictionByDateTime(String carparkID,String year,String month, String day, String hour, String minute, Response.Listener successListener, Response.ErrorListener errorListener){
        mQueue = Volley.newRequestQueue(context);
        // 0 means for the current day itself only
        String url = context.getResources().getString(R.string.serverUrl) + "/client/carparkdetails/prediction/" + carparkID + "/0?date=" + year + "-" + ((month.length() == 1) ? "0"+month : month) + "-" + ((day.length() == 1) ? "0"+day : day) + "T" + ((hour.length() == 1) ? "0"+hour : hour) + ":" + ((minute.length() == 1) ? "0"+minute : minute);
        JsonObjectRequest request = new JsonObjectRequest(url, null, successListener,errorListener);
        mQueue.add(request);
    }

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
