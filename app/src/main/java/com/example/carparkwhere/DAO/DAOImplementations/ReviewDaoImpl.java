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
import com.example.carparkwhere.Entities.Review;
import com.example.carparkwhere.R;
import com.example.carparkwhere.Interfaces.NetworkCallEventListener;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * This is the implementation class of the ReviewDao class
 * @author kohsweesen
 */
public class ReviewDaoImpl implements ReviewDao {

    /**
     * This queue is used to make network call request using Volley
     */
    private RequestQueue mQueue;
    /**
     * This is the context to be used for making network call request
     */
    private Context context;

    /**
     * This is the initialiser for this class
     * @param context the context to be passed in for making network call request
     */
    public ReviewDaoImpl(Context context){
        this.context = context;
    }


    //return type: ArrayList<Review>
    //note that empty array is still considered as success

    /**
     * This function returns all the carpark reviews of a specified carpark
     * @param carparkID the carpark id of the specified carpark
     * @param networkCallEventListener The network call event listener used to handle request response
     */
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

    /**
     * This function returns all the carpark reviews made by a specified user
     * @param userEmail the email address of the specified user
     * @param networkCallEventListener The network call event listener used to handle request response
     */
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


    /**
     * This function helps to save a new review into the database
     * @param review This is the review object to be saved into the database
     * @param networkCallEventListener The network call event listener used to handle request response
     */
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
            jsonBody.put("date",review.getDate());
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


    /**
     * This function helps to update a specified existing review with new values, such as updating comment or updating with new rating
     * @param oldReviewID this is the review id of the review before modification
     * @param newReview this is the new review object to be saved into the database
     * @param networkCallEventListener The network call event listener used to handle request response
     */
    public void updateCarparkReviewWithNewValues(String oldReviewID ,Review newReview, final NetworkCallEventListener networkCallEventListener){

        String URL = context.getResources().getString(R.string.serverUrl) + "/client/reviews/" + oldReviewID;
        JSONObject jsonBody = new JSONObject();
        mQueue = Volley.newRequestQueue(context);

        try{
            jsonBody.put("newRating", newReview.getRating());
            jsonBody.put("newDisplayName", newReview.getUserDisplayName());
            jsonBody.put("newComment", newReview.getComment());
            jsonBody.put("newDate",newReview.getDate());
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

    /**
     * This function deletes a specified carpark review by specifying the review id.
     * @param reviewID The review id of the review to be deleted
     * @param networkCallEventListener The network call event listener used to handle request response
     */
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

    /**
     * This function returns the average rating of the carpark.
     * @param carparkID The carpark id of the specified carpark
     * @param networkCallEventListener The network call event listener used to handle request response
     */
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

    /**
     * This function returns the number of reviews of a specified carpark
     * @param carparkID the carpark id of the specified carpark
     * @param networkCallEventListener The network call event listener used to handle request response
     */
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
