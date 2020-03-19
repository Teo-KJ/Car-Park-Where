package com.example.carparkwhere.Utilities;
import android.content.Context;
<<<<<<< Updated upstream

=======
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
>>>>>>> Stashed changes
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carparkwhere.Models.Carpark;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
<<<<<<< Updated upstream


=======
import com.google.gson.JsonObject;
>>>>>>> Stashed changes
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
<<<<<<< Updated upstream

//import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
=======
import java.util.HashMap;
import java.util.Map;

>>>>>>> Stashed changes

public class ServerInterfaceManager {
    private static RequestQueue mQueue;

    public static void getCarparkDetailsByID(Context context, String carparkID, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = "http://3.14.70.180:3002/client/carparkdetails/staticdetails/" + carparkID;
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

    public static void getAllCarparkCoordinates(Context context, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = "http://3.14.70.180:3002/client/carparkdetails/brief";
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

    public static void getCarparkWholeDayPredictedAvailability(Context context, String carparkID, Response.Listener successListener, Response.ErrorListener errorListener){
        mQueue = Volley.newRequestQueue(context);
        String url = "http://3.14.70.180:3002/client/carparkdetails/prediction/" + carparkID;
        JsonArrayRequest request = new JsonArrayRequest(url, successListener, errorListener);
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
