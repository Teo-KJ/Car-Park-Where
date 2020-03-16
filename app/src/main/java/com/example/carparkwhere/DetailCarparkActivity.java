package com.example.carparkwhere;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carparkwhere.Models.CarparkJson;
import com.example.carparkwhere.Utilities.NetworkCallEventListener;
import com.example.carparkwhere.Utilities.ServerInterfaceManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class DetailCarparkActivity extends AppCompatActivity {
    private TextView parkingRates_TV, carparkNumber_TV, carparkAddress_TV, testTV;
    private ImageButton bookmarkToggle_IMGBTN, submitReview_IMGBTN, backDetailCarparkActivity_IMGBTN;
    private Button viewCarparkReviews_BTN;
    private ProgressDialog nDialog;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_carpark);

        parkingRates_TV = findViewById(R.id.carparkPrices);
        carparkNumber_TV = findViewById(R.id.carparkNumber);
        carparkAddress_TV = findViewById(R.id.carparkAddress);
        bookmarkToggle_IMGBTN = findViewById(R.id.BookmarkButton);
        viewCarparkReviews_BTN = findViewById(R.id.totalNumOfReviews);
        //ImageButton bookmarkToggle_IMGBTN;
        //Button ;

        //  Dialogue bar
        presentProgressDialog("Loading Carpark...");

        //  Back button to return to previous activity
        backDetailCarparkActivity_IMGBTN = findViewById(R.id.back_button);
        backDetailCarparkActivity_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //  Make a review by selecting this button, which goes to SubmitReviewActivity
        submitReview_IMGBTN = findViewById(R.id.makeReviewButton);
        submitReview_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailCarparkActivity.this, SubmitReviewActivity.class));
            }
        });

        //  With ServerInterfaceManager, get the carpark detail from the carpark details server.
        Intent intent = getIntent();
        String str = intent.getStringExtra("CARPARK_ID");
        ServerInterfaceManager.getCarparkDetailsByID(this, str, new NetworkCallEventListener() {
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

        barChart = findViewById(R.id.visualisation);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(44f, 0));
        barEntries.add(new BarEntry(68f, 1));
        barEntries.add(new BarEntry(100f, 2));
        BarDataSet dataSet = new BarDataSet(barEntries, "Time");

        ArrayList<String> allTimings = new ArrayList<String>();
        allTimings.add("01:00");
        allTimings.add("13:00");
        allTimings.add("20:00");

        BarData data = new BarData(allTimings, dataSet);
        barChart.setData(data);
        barChart.setTouchEnabled(true);

        testTV = findViewById(R.id.test_Text);
        testTV.setText("Predicted Availability");

        ServerInterfaceManager.getCarparkWholeDayPredictedAvailability(this, str, new Response.Listener() {
            @Override
            public void onResponse(Object response){
                JSONArray jsonArray = (JSONArray) response;
                for (int j=0; j<jsonArray.length(); j++){
                    try {
                        JSONObject predictions = jsonArray.getJSONObject(j);
                        System.out.println(predictions);
                        String time = predictions.getString("time");
                        int carparkPrediction = predictions.getInt("predictedAvailability");

                        //allTimings.add(time);
                        //allPredictedAvailability.add(carparkPrediction);
                        testTV.append("\n" + time + " " + carparkPrediction);

                        //barEntries.add(new BarEntry(carparkPrediction, j));
                        //barEntries.add(new BarEntry(100f, j));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

    }

    //   Progress bar to load carpark detail
    private void presentProgressDialog(String message){
        nDialog = new ProgressDialog(DetailCarparkActivity.this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle(message);
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }

}