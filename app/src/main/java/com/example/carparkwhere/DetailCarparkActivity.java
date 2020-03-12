package com.example.carparkwhere;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.example.carparkwhere.Utilities.NetworkCallEventListener;
import com.example.carparkwhere.Utilities.ServerInterfaceManager;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailCarparkActivity extends AppCompatActivity {
    private TextView parkingRates_TV, carparkNumber_TV, carparkAddress_TV;
    private ProgressDialog nDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_carpark);
        presentProgressDialog("Loading Carpark...");
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

        ImageButton bookmarkToggle_IMGBTN;
        RatingBar rating_RBAR;
        Button viewCarparkReviews_BTN;

        ServerInterfaceManager.getCarparkDetailsByID(this, "A78", new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                nDialog.dismiss();
                CarparkJson carparkJson = (CarparkJson) networkCallResult;
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
        });

    }

    private void presentProgressDialog(String message){
        nDialog = new ProgressDialog(DetailCarparkActivity.this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle(message);
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }

}
