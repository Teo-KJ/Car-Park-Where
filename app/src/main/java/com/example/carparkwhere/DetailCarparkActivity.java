package com.example.carparkwhere;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.carparkwhere.Models.Carpark;
import com.example.carparkwhere.Utilities.NetworkCallEventListener;
import com.example.carparkwhere.Utilities.ServerInterfaceManager;
import com.example.carparkwhere.Utilities.UserDataManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.firestore.auth.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class DetailCarparkActivity extends AppCompatActivity {
    private TextView parkingRates_TV, carparkNumber_TV, carparkAddress_TV, testTV;
    private ImageButton bookmarkToggle_IMGBTN, submitReview_IMGBTN, backDetailCarparkActivity_IMGBTN, tutorial_IMGBTN;
    private Button viewCarparkReviews_BTN;
    private ProgressDialog nDialog;
    private BarChart barChart;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_carpark);

        Resources res = getResources();
        int day = 0;
        parkingRates_TV = findViewById(R.id.carparkPrices);
        carparkNumber_TV = findViewById(R.id.carparkNumber);
        carparkAddress_TV = findViewById(R.id.carparkAddress);
        bookmarkToggle_IMGBTN = findViewById(R.id.BookmarkButton);
        viewCarparkReviews_BTN = findViewById(R.id.totalNumOfReviews);
        bookmarkToggle_IMGBTN = findViewById(R.id.BookmarkButton);
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

        tutorial_IMGBTN = findViewById(R.id.tutorialButton);
        tutorial_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
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
        final String str = intent.getStringExtra("CARPARK_ID"); // Get the carpark number
        ServerInterfaceManager.getCarparkDetailsByID(this, str, new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                nDialog.dismiss();
                Carpark carpark = (Carpark) networkCallResult;
                carparkNumber_TV.setText(carpark.carparkNo);
                carparkAddress_TV.setText(carpark.carparkName);
                ArrayList<Carpark.CarparkCarDetails.CarparkPriceJson> allPrices = carpark.carDetails.prices;
                Carpark.CarparkCarDetails.CarparkPriceJson prices = allPrices.get(0);
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

        /*bookmarkToggle_IMGBTN = findViewById(R.id.BookmarkButton);
        bookmarkToggle_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDataManager.addNewFavouriteCarpark(str);
            }
        });*/

        // Bar chart for the visualisation
        barChart = findViewById(R.id.visualisation);
        //final ArrayList<BarEntry> barEntries = new ArrayList<>();
        //final ArrayList<String> allTimings = new ArrayList<String>();

        barChart.setNoDataText("Loading the data...");
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisRight().setDrawLabels(false);

        // With ServerInterfaceManager, get the predicted number of carpark lots.
        ServerInterfaceManager.getCarparkWholeDayPredictedAvailability(this, str, new Response.Listener() {
            @Override
            public void onResponse(Object response){
                JSONArray jsonArray = (JSONArray) response;
                ArrayList<BarEntry> barEntries = new ArrayList<>();
                ArrayList<String> allTimings = new ArrayList<String>();

                for (int j=0; j<jsonArray.length(); j++){
                    try {
                        JSONObject predictions = jsonArray.getJSONObject(j);
                        String time = predictions.getString("time");
                        float carparkPrediction = predictions.getInt("predictedAvailability");
                        barEntries.add(new BarEntry(carparkPrediction, j));
                        allTimings.add(time);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                BarDataSet dataSet = new BarDataSet(barEntries, "Time");
                BarData data = new BarData(allTimings, dataSet);
                barChart.setData(data);
                barChart.setTouchEnabled(true);
                barChart.invalidate();
                barChart.refreshDrawableState();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        spinner = findViewById(R.id.daysDropdownBox);
        //final String[] daysOfTheWeek = res.getStringArray(R.array.daysInWeek);
        for (int i=0; i<7; i++)
            if (i==identifyDay()) day=i;

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.daysInWeek, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(day);

        // Selected day on dropdown box
        /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==today){
                    BarDataSet dataSet = new BarDataSet(barEntries, "Time");
                    BarData data = new BarData(allTimings, dataSet);
                    barChart.setData(data);
                    barChart.setTouchEnabled(true);
                    barChart.invalidate();
                    barChart.refreshDrawableState();
                }
                else{
                    barChart.setData(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

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

    // Identify the day of the week
    private int identifyDay (){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        return calendar.get(Calendar.DAY_OF_WEEK) % 7-2;
    }

    public void openDialog() {
        Dialog help = new Dialog(DetailCarparkActivity.this);
        Button okButton = findViewById(R.id.okButton);
        help.requestWindowFeature(Window.FEATURE_NO_TITLE);
        help.setContentView(R.layout.detail_carpark_tutorial);
        help.show();
        /*okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
    }

}