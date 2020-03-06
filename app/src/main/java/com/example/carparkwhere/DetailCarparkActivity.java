package com.example.carparkwhere;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.android.volley.toolbox.Volley;

import com.example.carparkwhere.Models.CarparkJson;
import com.example.carparkwhere.Utilities.ServerInterfaceManager;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DetailCarparkActivity extends AppCompatActivity {
    private TextView parkingRates_TV, carparkNumber_TV, carparkAddress_TV;
//    private RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_carpark);

        ImageButton backDetailCarparkActivity_IMGBTN = findViewById(R.id.back_button);
        backDetailCarparkActivity_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        parkingRates_TV = findViewById(R.id.carparkPrices);
        carparkNumber_TV = findViewById(R.id.carparkNumber);
        carparkAddress_TV = findViewById(R.id.carparkAddress);
        ImageButton submitReview_IMGBTN = findViewById(R.id.makeReviewButton);

//        mQueue = Volley.newRequestQueue(this);

//        jsonParse();

//        submitReview_IMGBTN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                jsonParse();
//            }
//        });

        ImageButton bookmarkToggle_IMGBTN;
        RatingBar rating_RBAR;
        Button viewCarparkReviews_BTN;

        ServerInterfaceManager.getCarparkDetailsByID(this,"A78", new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Gson gson = new Gson();
                CarparkJson carparkJson = gson.fromJson(response.toString(),CarparkJson.class);
                carparkNumber_TV.setText(carparkJson.carparkNo);
                carparkAddress_TV.setText(carparkJson.carparkName);
                ArrayList<CarparkJson.CarparkCarDetailsJson.CarparkPriceJson> allPrices = carparkJson.carDetails.prices;
                CarparkJson.CarparkCarDetailsJson.CarparkPriceJson prices = allPrices.get(0);
                String description = prices.description;
                String price = prices.price;
                parkingRates_TV.setText(description + "\n" + price);

                for (int i=1; i<allPrices.size(); i++){
                    prices = allPrices.get(i);
                    description = prices.description;
                    price = prices.price;
                    parkingRates_TV.append("\n\n" + description + "\n" + price);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });




    }
//
//    private void jsonParse() {
//        String url = "http://3.14.70.180:3002/client/carparkdetails";
//        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        try{
//                            for (int j=0; j<response.length(); j++){
//                                JSONObject carparkJson = response.getJSONObject(j);
//                                String carparkNumber = carparkJson.getString("carparkNo");
//                                if (carparkNumber.equalsIgnoreCase("D0004")){
//                                    String address = carparkJson.getString("carparkName");
//
//                                    JSONArray allPrices = carparkJson.getJSONArray("prices");
//                                    JSONObject prices = allPrices.getJSONObject(0);
//                                    String description = prices.getString("description");
//                                    String price = prices.getString("price");
//                                    parkingRates_TV.setText(description + "\n" + price);
//
//                                    for (int i=1; i<allPrices.length(); i++){
//                                        prices = allPrices.getJSONObject(i);
//                                        description = prices.getString("description");
//                                        price = prices.getString("price");
//                                        parkingRates_TV.append("\n\n" + description + "\n" + price);
//                                    }
//
//                                    carparkNumber_TV.setText(carparkNumber);
//                                    carparkAddress_TV.setText(address);
//                                }
//                            }
//
//                        }
//                        catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                    }
//                }
//        );
//
//        mQueue.add(request);
    }
