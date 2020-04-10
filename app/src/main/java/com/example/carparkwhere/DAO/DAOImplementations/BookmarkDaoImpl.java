package com.example.carparkwhere.DAO.DAOImplementations;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carparkwhere.DAO.DAOInterfaces.BookmarkDao;
import com.example.carparkwhere.R;
import com.example.carparkwhere.Interfaces.NetworkCallEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookmarkDaoImpl implements BookmarkDao {

    private RequestQueue mQueue;
    private Context context;

    public BookmarkDaoImpl(Context context){
        this.context = context;
    }

    public void saveUserCarparkBookmark(ArrayList<String> carparkIds, String userEmail, final NetworkCallEventListener networkCallEventListener){
        String URL = context.getResources().getString(R.string.serverUrl) + "/client/bookmarks/save";
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
    public void getUserBookmarkCarparkIds(String userEmail, final NetworkCallEventListener networkCallEventListener){
        mQueue = Volley.newRequestQueue(context);
        String url = context.getResources().getString(R.string.serverUrl) + "/client/bookmarks/" + userEmail;
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
