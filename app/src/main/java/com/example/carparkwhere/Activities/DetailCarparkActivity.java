/**
 * This Java class is used to generate the carpark details when
 * the user intends to view based on individual carparks.
 * @author Kai Jie, Swee Sen
 * @version 1.0
 */

package com.example.carparkwhere.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.carparkwhere.DAO.DAOImplementations.BookmarkDaoImpl;
import com.example.carparkwhere.DAO.DAOImplementations.CarparkDaoImpl;
import com.example.carparkwhere.DAO.DAOImplementations.ReviewDaoImpl;
import com.example.carparkwhere.DAO.DAOImplementations.UserDataDaoFirebaseImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.BookmarkDao;
import com.example.carparkwhere.DAO.DAOInterfaces.CarparkDao;
import com.example.carparkwhere.DAO.DAOInterfaces.ReviewDao;
import com.example.carparkwhere.DAO.DAOInterfaces.UserDataDao;
import com.example.carparkwhere.Exceptions.UserNotLoggedInException;
import com.example.carparkwhere.Entities.Carpark;
import com.example.carparkwhere.Interfaces.NetworkCallEventListener;
import com.example.carparkwhere.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DetailCarparkActivity extends AppCompatActivity {
    // Variables to generate the various details in texts.
    private TextView parkingRates_TV, carparkNumber_TV, carparkAddress_TV, timeAdvice_TV, averageRating_TV, totalReviews_TV, liveAvailaibility_TV, capacity_TV;
    // Buttons for the various features.
    private ImageButton bookmarkToggle_IMGBTN, submitReview_IMGBTN, backDetailCarparkActivity_IMGBTN, tutorial_IMGBTN, detailDirection_IMGBTN;
    // Rating bar for the average rating of the carpark.
    public RatingBar averageRatingInStars;
    // Progress dialogue for the user to understand the page is still loading.
    private ProgressDialog nDialog;
    // Bar chart to show the visualisation of lots in the day.
    private BarChart barChart;
    // Dropdown box for the user to choose the date.
    private Spinner spinner;
    private ArrayList<String> userBookmarkCarparks;
    // Condition if the user has bookmarked the particular carpark.
    private boolean userBookmarkedThis = false;
    private CarparkDao carparkDaoHelper;
    private ReviewDao reviewDaoHelper;
    private BookmarkDao bookmarkDaoHelper;
    private UserDataDao userDataDaoHelper;
    // Carpark lot capacity.
    float carparkCapacity;
    // Current day of the week.
    int currentDay = identifyDay();

    /**
     * onCreate is used to initialise the activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_carpark);

        carparkDaoHelper = new CarparkDaoImpl(this);
        reviewDaoHelper = new ReviewDaoImpl(this);
        bookmarkDaoHelper = new BookmarkDaoImpl(this);
        userDataDaoHelper = new UserDataDaoFirebaseImpl();

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#111111")));
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("");

        // Link variables with the items in XML file
        parkingRates_TV = findViewById(R.id.carparkPrices);
        carparkNumber_TV = findViewById(R.id.carparkNumber);
        carparkAddress_TV = findViewById(R.id.carparkAddress);
        bookmarkToggle_IMGBTN = findViewById(R.id.BookmarkButton);
        detailDirection_IMGBTN = findViewById(R.id.directionsButton);
        averageRating_TV = findViewById(R.id.averageRating);
        averageRatingInStars = findViewById(R.id.averageRatingInStars);
        totalReviews_TV = findViewById(R.id.totalNumOfReviews);
        liveAvailaibility_TV = findViewById(R.id.liveAvailability);
        timeAdvice_TV = findViewById(R.id.suggestedTimeToPark);
        capacity_TV = findViewById(R.id.capacity_tv);

        //  Dialogue bar to load the carpark details
        presentProgressDialog("Loading Carpark...");

        //  Back button to return to previous activity
        backDetailCarparkActivity_IMGBTN = findViewById(R.id.back_button);
        backDetailCarparkActivity_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // "Question mark" button to serve as tutorial for the user
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
                Intent intent = new Intent(DetailCarparkActivity.this, SubmitReviewActivity.class);
                intent.putExtra("carparkid",getIntent().getStringExtra("CARPARK_ID"));
                startActivity(intent);
            }
        });

        // Provide the directions to the carpark based on the user's current location.
        detailDirection_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                double latitude = intent.getDoubleExtra("Lat", 0.0);
                double longitude = intent.getDoubleExtra("Lng", 0.0);
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        // Getting the user bookmarks, so that the user bookmarked carpark is saved under his/her profile
        try{
            bookmarkDaoHelper.getUserBookmarkCarparkIds(userDataDaoHelper.getUserEmail(), new NetworkCallEventListener() {
                @Override
                public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                    if (isSuccessful){
                        userBookmarkCarparks = (ArrayList<String>) networkCallResult;
                        if (userBookmarkCarparks != null){
                            if (userBookmarkCarparks.contains(getIntent().getStringExtra("CARPARK_ID"))){
                                userBookmarkedThis = true;
                                bookmarkToggle_IMGBTN.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                            }
                        }
                    }
                }
            });
        }
        catch (UserNotLoggedInException e){

        }

        // Getting the current Availability
        carparkDaoHelper.getCarparkLiveAvailability(getIntent().getStringExtra("CARPARK_ID"), new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                if (isSuccessful){
                    Integer liveAvailability = (Integer) networkCallResult;
                    liveAvailaibility_TV.setText(liveAvailability + "");
                }
            }
        });

        // Remove the visibility of the bookmark and submit review button for guest user
        if (!userDataDaoHelper.isLoggedIn()){
            bookmarkToggle_IMGBTN.setVisibility(View.INVISIBLE);
            submitReview_IMGBTN.setVisibility(View.INVISIBLE);
        }

        // User selects this button to bookmark a carpark
        bookmarkToggle_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentProgressDialog("Changing Bookmark...");
              
                if (userBookmarkCarparks == null){
                    userBookmarkCarparks = new ArrayList<>();
                }

                if (userBookmarkedThis){
                    userBookmarkCarparks.remove(getIntent().getStringExtra("CARPARK_ID"));
                }else{
                    userBookmarkCarparks.add(getIntent().getStringExtra("CARPARK_ID"));
                }

                try{
                    bookmarkDaoHelper.saveUserCarparkBookmark(userBookmarkCarparks, userDataDaoHelper.getUserEmail(), new NetworkCallEventListener() {
                        @Override
                        public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                            nDialog.dismiss();
                            if (isSuccessful){
                                userBookmarkedThis = !userBookmarkedThis;
                                if (userBookmarkedThis){
                                    bookmarkToggle_IMGBTN.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
                                }else{
                                    bookmarkToggle_IMGBTN.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star));
                                }
                                Toast.makeText(DetailCarparkActivity.this,userBookmarkedThis ? "Added bookmark!" : "Removed bookmark!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(DetailCarparkActivity.this,"Error occured, try again!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                catch (UserNotLoggedInException e){

                }
            }
        });

        /**
         * With server interface manager get average ratings of the carpark and display it.
         */
        reviewDaoHelper.getCarparkAverageRating(getIntent().getStringExtra("CARPARK_ID"), new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                if (isSuccessful){
                    Double rating = (Double) networkCallResult;
                    averageRatingInStars.setRating(rating.floatValue());
                    averageRating_TV.setText(String.valueOf((Math.round(rating*100.0))/100.0));
                }else{
                    averageRatingInStars.setRating(0);
                    averageRating_TV.setText("0.0");
                }
            }
        });

        /**
         * Get the directions to the carpark from the current location.
         */
        detailDirection_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                double latitude = intent.getDoubleExtra("Lat", 0.0);
                double longitude = intent.getDoubleExtra("Lng", 0.0);
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
      
        /**
         * Get the number of reviews for the particular carpark.
         */
        reviewDaoHelper.getCarparkReviewsCount(getIntent().getStringExtra("CARPARK_ID"), new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                if (isSuccessful){
                    totalReviews_TV.setText(((Integer) networkCallResult) + " review" + (((Integer) networkCallResult) > 1 ? "s" : ""));
                }else{
                    totalReviews_TV.setText("0 review");
                }
            }
        });

        /**
         * View all the reviews made for the particular carpark.
         */
        totalReviews_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailCarparkActivity.this, CarparkReviewsActivity.class);
                intent.putExtra("carparkid", getIntent().getStringExtra("CARPARK_ID"));
                startActivity(intent);
            }
        });

        //  With carparkDaoHelper, get the carpark detail from the carpark details server.
        Intent intent = getIntent();
        final String str = intent.getStringExtra("CARPARK_ID"); // Get the carpark number
        carparkDaoHelper.getCarparkDetailsByID(str, new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                nDialog.dismiss();
                Carpark carpark = (Carpark) networkCallResult;
                // Get the carpark number and address
                carparkNumber_TV.setText(carpark.carparkNo);
                carparkAddress_TV.setText(carpark.carparkName);
                try{
                    capacity_TV.setText(Integer.toString(carpark.carDetails.capacity));
                    carparkCapacity = carpark.carDetails.capacity;
                    //getAvailabilityPredictionData(1, str); // This is the line that caused the bug where day increased by 1!
                }catch(Exception e){
                    capacity_TV.setText("");
                }

                // Get the carpark prices
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

        spinner = findViewById(R.id.daysDropdownBox);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.daysInWeek, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Set the current dropdown box item to be the current day of the week
        spinner.setSelection(currentDay);

        // Selected day on dropdown box
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int difference = i - currentDay;
                getAvailabilityPredictionData(difference, str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                int difference = 0;
                getAvailabilityPredictionData(difference, str);
            }
        });

        // Bar chart for the visualisation
        barChart = findViewById(R.id.visualisation);
        barChart.setNoDataText("Loading the data...");
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.setDescription("");
    }

    /**
     * Function for Progress bar to load carpark detail
     * @param message is the string of words to be displayed while loading the page.
     */
    private void presentProgressDialog(String message){
        nDialog = new ProgressDialog(DetailCarparkActivity.this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle(message);
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }

    /**
     * Function to identify the day of the week
     * @return The current day of the week in integer.
     */
    private int identifyDay (){
        Date today = getCurrentTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        return (calendar.get(Calendar.DAY_OF_WEEK) - 2) % 7;
    }

    /**
     * Function to open up the help dialogue.
     */
    public void openDialog() {
        Dialog help = new Dialog(DetailCarparkActivity.this);
        help.requestWindowFeature(Window.FEATURE_NO_TITLE);
        help.setContentView(R.layout.detail_carpark_tutorial);
        help.show();
    }

    /**
     * This function gets the predicted number of carpark lots.
     * @param increment is the integer where the number from current day of week has been incremented by.
     * @param carparkNumber is the unique carpark number.
     */
    private void getAvailabilityPredictionData(Integer increment, String carparkNumber){
        carparkDaoHelper.getCarparkWholeDayPredictedAvailability(carparkNumber, increment, new Response.Listener() {
            @Override
            // Get the JSON object of the carpark's details from the API
            public void onResponse(Object response) {
                JSONArray jsonArray = (JSONArray) response;
                ArrayList<BarEntry> barEntries = new ArrayList<>();
                ArrayList<String> allTimings = new ArrayList<String>();
                ArrayList<String> allSuggestedTimes = new ArrayList<String>();
                StringBuilder stringBuilder = new StringBuilder();
                int counter = 5;

                // Take all the predicted lots and its time from the API, then append to ArrayList for display
                for (int j = 0; j < jsonArray.length(); j++) {
                    try {
                        JSONObject predictions = jsonArray.getJSONObject(j);
                        String time = predictions.getString("time");
                        float carparkPrediction = predictions.getInt("predictedAvailability");

                        // Provide up to 5 suggested upcoming timings to park (counter = 5)
                        if ((convertTimeString(time).compareTo(getCurrentTime()) > 0) && (carparkPrediction / carparkCapacity >= 0.5) &&
                                (j < counter)) {
                            allSuggestedTimes.add(time);
                        }

                        barEntries.add(new BarEntry(carparkPrediction, j));
                        allTimings.add(time);
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                }

                // The bar chart visualisation of the entire day's predicted lots.
                BarDataSet dataSet = new BarDataSet(barEntries, "Time");
                BarData data = new BarData(allTimings, dataSet);
                barChart.setData(data);
                barChart.setTouchEnabled(true);
                barChart.invalidate();
                barChart.refreshDrawableState();

                // Provide suggested times to park from current time of accessing the app.
                for (String time : allSuggestedTimes) {
                    stringBuilder.append(time + "\n");
                }
                // If there is no suggested time to give, display as 'None'
                if (allSuggestedTimes.size() == 0) timeAdvice_TV.setText("None");
                else timeAdvice_TV.setText(stringBuilder);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This function converts the date in Date object to a String object.
     * @param time
     * @return date in String
     * @throws ParseException
     */
    private Date convertTimeString (String time) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        String date = currentDate + " " + time;
        return formatter.parse(date);
    }

    /**
     * This function is to get the current time.
     * @return the current date in Data object.
     */
    private Date getCurrentTime(){
        return java.util.Calendar.getInstance().getTime();
    }

    /**
     * 
     */
    @Override
    protected void onResume() {
        super.onResume();

        reviewDaoHelper.getCarparkReviewsCount(getIntent().getStringExtra("CARPARK_ID"), new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                if (isSuccessful){
                    totalReviews_TV.setText(((Integer) networkCallResult) + " review" + (((Integer) networkCallResult) > 1 ? "s" : ""));
                }else{
                    totalReviews_TV.setText("0 review");
                }
            }
        });

        reviewDaoHelper.getCarparkAverageRating(getIntent().getStringExtra("CARPARK_ID"), new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                if (isSuccessful){
                    Double rating = (Double) networkCallResult;
                    averageRatingInStars.setRating(rating.floatValue());
                    averageRating_TV.setText(String.valueOf((Math.round(rating*100.0))/100.0));
                }else{
                    averageRatingInStars.setRating(0);
                    averageRating_TV.setText("0.0");
                }
            }
        });
    }
}