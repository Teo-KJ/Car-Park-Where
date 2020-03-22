package com.example.carparkwhere.DAO.DAOImplementations;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carparkwhere.DAO.DAOInterfaces.ReviewDao;
import com.example.carparkwhere.ModelObjects.Review;
import com.example.carparkwhere.R;
import com.example.carparkwhere.FilesIdkWhereToPutYet.NetworkCallEventListener;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewDaoImpl implements ReviewDao {

    private RequestQueue mQueue;
    private Context context;

    public ReviewDaoImpl(Context context){
        this.context = context;
    }


    //return type: ArrayList<Review>
    //note that empty array is still considered as success
    public void getCarparkReviewsByCarparkID(String carparkID, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = context.getResources().getString(R.string.serverUrl) + "/client/reviews/bycarpark/" + carparkID;
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
    public void getCarparkReviewsByUserEmail(String userEmail, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = context.getResources().getString(R.string.serverUrl) + "/client/reviews/byuser/" + userEmail;
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



    public void saveNewCarparkReview(Review review, final NetworkCallEventListener networkCallEventListener){

        String URL = context.getResources().getString(R.string.serverUrl) + "/client/reviews/save/";
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


    public void updateCarparkReviewWithNewValues(String oldReviewID ,Review newReview, final NetworkCallEventListener networkCallEventListener){

        String URL = context.getResources().getString(R.string.serverUrl) + "/client/reviews/" + oldReviewID;
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

    public void deleteCarparkReviewByReviewID(String reviewID, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = context.getResources().getString(R.string.serverUrl) + "/client/reviews/" + reviewID;
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
    public void getCarparkAverageRating(String carparkID, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = context.getResources().getString(R.string.serverUrl) + "/client/reviews/averagerating/" + carparkID;
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
    public void getCarparkReviewsCount(String carparkID, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = context.getResources().getString(R.string.serverUrl) + "/client/reviews/totalrating/" + carparkID;
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
}
