package com.example.carparkwhere.Utilities;
import android.content.Context;
import android.util.Pair;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carparkwhere.Models.CarparkJson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;

public class ServerInterfaceManager {


    private static RequestQueue mQueue;


    public static void getCarparkDetailsByID(Context context, String carparkID, Response.Listener successListener, Response.ErrorListener errorListener){
        mQueue = Volley.newRequestQueue(context);
        String url = "http://3.14.70.180:3002/client/carparkdetails/staticdetails/" + carparkID;
        JsonObjectRequest request = new JsonObjectRequest(url, null, successListener,errorListener);
        mQueue.add(request);
    }

    public static void getAllCarparkCoordinates(Context context,Response.Listener successListener, Response.ErrorListener errorListener){
        mQueue = Volley.newRequestQueue(context);
        String url = "http://3.14.70.180:3002/client/carparkdetails/brief";
        JsonArrayRequest request = new JsonArrayRequest(url,successListener,errorListener);
        mQueue.add(request);
    }

    public static void getCarparkWholeDayPredictedAvailability(Context context, String carparkID,Response.Listener successListener, Response.ErrorListener errorListener){
        mQueue = Volley.newRequestQueue(context);
        String url = "http://3.14.70.180:3002/client/carparkdetails/prediction/" + carparkID;
        JsonObjectRequest request = new JsonObjectRequest(url, null, successListener,errorListener);
        mQueue.add(request);
    }

    public static void getCarparkAvailabilityPredictionByDateTime(Context context, String carparkID,String year,String month, String day, String hour, String minute, Response.Listener successListener, Response.ErrorListener errorListener){
        mQueue = Volley.newRequestQueue(context);
        String url = "http://3.14.70.180:3002/client/carparkdetails/prediction/" + carparkID + "?date=" + year + "-" + ((month.length() == 1) ? "0"+month : month) + "-" + ((day.length() == 1) ? "0"+day : day) + "T" + ((hour.length() == 1) ? "0"+hour : hour) + ":" + ((minute.length() == 1) ? "0"+minute : minute);
        JsonObjectRequest request = new JsonObjectRequest(url, null, successListener,errorListener);
        mQueue.add(request);
    }

    public static void getCarparkLiveAvailability(Context context, String carparkID, Response.Listener successListener, Response.ErrorListener errorListener){
        mQueue = Volley.newRequestQueue(context);
        String url = "http://3.14.70.180:3002/client/carparkdetails/liveavailability/" + carparkID;
        JsonObjectRequest request = new JsonObjectRequest(url, null, successListener,errorListener);
        mQueue.add(request);
    }



}
