package com.example.carparkwhere.Utilities;
import android.app.DownloadManager;
import android.content.Context;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carparkwhere.Models.Carpark;
import com.example.carparkwhere.Models.Review;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashMap;
import java.util.Map;


public class ServerInterfaceManager {
    private static RequestQueue mQueue;

    //return type: Carpark
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


    //return type: ArrayList<Carpark>
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

    public static void getCarparkWholeDayPredictedAvailability(Context context, String carparkID, Integer increment, Response.Listener successListener, Response.ErrorListener errorListener){
        mQueue = Volley.newRequestQueue(context);
        String url = "http://3.14.70.180:3002/client/carparkdetails/prediction/" + carparkID + "/" + String.valueOf(increment);
        JsonArrayRequest request = new JsonArrayRequest(url, successListener, errorListener);
        mQueue.add(request);
    }

    public static void getCarparkAvailabilityPredictionByDateTime(Context context, String carparkID,String year,String month, String day, String hour, String minute, Response.Listener successListener, Response.ErrorListener errorListener){
        mQueue = Volley.newRequestQueue(context);
        String url = "http://3.14.70.180:3002/client/carparkdetails/prediction/" + carparkID + "?date=" + year + "-" + ((month.length() == 1) ? "0"+month : month) + "-" + ((day.length() == 1) ? "0"+day : day) + "T" + ((hour.length() == 1) ? "0"+hour : hour) + ":" + ((minute.length() == 1) ? "0"+minute : minute);
        JsonObjectRequest request = new JsonObjectRequest(url, null, successListener,errorListener);
        mQueue.add(request);
    }

    public static void getCarparkLiveAvailability(Context context, String carparkID, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = "http://3.14.70.180:3002/client/carparkdetails/liveavailability/" + carparkID;
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
    public static void getCarparkReviewsByCarparkID(Context context,String carparkID, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = "http://3.14.70.180:3002/client/reviews/bycarpark/" + carparkID;
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

    //Return type: ArrayList<Review>
    public static void getCarparkReviewsByUserEmail(Context context,String userEmail, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = "http://3.14.70.180:3002/client/reviews/byuser/" + userEmail;
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



    public static void saveNewCarparkReview(Context context, Review review, final NetworkCallEventListener networkCallEventListener){

        String URL = "http://3.14.70.180:3002/client/reviews/save/";
        JSONObject jsonBody = new JSONObject();
        mQueue = Volley.newRequestQueue(context);

        try{
            jsonBody.put("rating", review.getRating());
            jsonBody.put("email", review.getUserEmail());
            jsonBody.put("displayName", review.getUserDisplayName());
            jsonBody.put("carparkId", review.getCarparkId());
            jsonBody.put("comment", review.getComment());
        }catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                networkCallEventListener.onComplete("Success",true,null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                networkCallEventListener.onComplete("Failure",false,error.getMessage());
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> headers = new HashMap<>();
                return headers;
            }
        };
        mQueue.add(request);
    }


    public static void updateCarparkReviewWithNewValues(Context context,String oldReviewID ,Review newReview, final NetworkCallEventListener networkCallEventListener){

        String URL = "http://3.14.70.180:3002/client/reviews/" + oldReviewID;
        JSONObject jsonBody = new JSONObject();
        mQueue = Volley.newRequestQueue(context);

        try{
            jsonBody.put("newRating", newReview.getRating());
            jsonBody.put("newDisplayName", newReview.getUserDisplayName());
            jsonBody.put("newComment", newReview.getComment());
        }catch (Exception e){
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH, URL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                networkCallEventListener.onComplete("Success",true,null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                networkCallEventListener.onComplete("Failure",false,error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> headers = new HashMap<>();
                return headers;
            }
        };
        mQueue.add(request);
    }

    public static void deleteCarparkReviewByReviewID(Context context,String reviewID, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = "http://3.14.70.180:3002/client/reviews/" + reviewID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE,url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                networkCallEventListener.onComplete("success",true,null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
                networkCallEventListener.onComplete("failure",false,error.getMessage());
            }
        });
        mQueue.add(request);
    }


    //Return Type: Double
    public static void getCarparkAverageRating(Context context,String carparkID, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = "http://3.14.70.180:3002/client/reviews/averagerating/" + carparkID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    Double averageRating = response.getDouble("avg");
                    networkCallEventListener.onComplete(averageRating,true,null);
                }catch (Exception e){
                    networkCallEventListener.onComplete(null,false,null);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                networkCallEventListener.onComplete("failure",false,error.getMessage());
            }
        });
        mQueue.add(request);
    }

    //Return Type: Integer
    public static void getCarparkReviewsCount(Context context,String carparkID, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = "http://3.14.70.180:3002/client/reviews/totalrating/" + carparkID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    Integer totalCount = response.getInt("sum");
                    networkCallEventListener.onComplete(totalCount,true,null);
                }catch (Exception e){
                    networkCallEventListener.onComplete(null,false,null);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                networkCallEventListener.onComplete("failure",false,error.getMessage());
            }
        });
        mQueue.add(request);
    }

    public static void saveUserCarparkBookmark(Context context, ArrayList<String> carparkIds, String userEmail, final NetworkCallEventListener networkCallEventListener){
        String URL = "http://3.14.70.180:3002/client/bookmarks/save";
        JSONObject jsonBody = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        mQueue = Volley.newRequestQueue(context);

        try{
            for (String carparkid:carparkIds){
                jsonArray.put(carparkid);
            }
            jsonBody.put("carparkIds", jsonArray);
            jsonBody.put("userEmail", userEmail);
        }catch (Exception e){
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                networkCallEventListener.onComplete("Success",true,null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                networkCallEventListener.onComplete("Failure",false,error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> headers = new HashMap<>();
                return headers;
            }
        };
        mQueue.add(request);
    }

    //return type ArrayList<String>
    public static void getUserBookmarkCarparkIds(Context context,String userEmail, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = "http://3.14.70.180:3002/client/bookmarks/" + userEmail;
        System.out.println(url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray carparkIdJsonArray = (JSONArray) response.getJSONArray("carparkIds");

                    ArrayList<String> carparkids = new ArrayList<>();

                    for (int i=0;i<carparkIdJsonArray.length();i++){
                        carparkids.add(carparkIdJsonArray.getString(i));
                    }
                    networkCallEventListener.onComplete(carparkids,true,null);
                }catch (Exception e){
                    e.printStackTrace();
                    networkCallEventListener.onComplete(null,false,null);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                networkCallEventListener.onComplete("failure",false,error.getMessage());
            }
        });
        mQueue.add(request);
    }



}
