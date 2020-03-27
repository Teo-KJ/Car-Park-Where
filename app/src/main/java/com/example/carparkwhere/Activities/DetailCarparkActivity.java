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
import com.example.carparkwhere.DAO.DAOImplementations.BookmarkDaoImpl;
import com.example.carparkwhere.DAO.DAOImplementations.CarparkDaoImpl;
import com.example.carparkwhere.DAO.DAOImplementations.ReviewDaoImpl;
import com.example.carparkwhere.DAO.DAOImplementations.UserDataDaoFirebaseImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.BookmarkDao;
import com.example.carparkwhere.DAO.DAOInterfaces.CarparkDao;
import com.example.carparkwhere.DAO.DAOInterfaces.ReviewDao;
import com.example.carparkwhere.DAO.DAOInterfaces.UserDataDao;
import com.example.carparkwhere.FilesIdkWhereToPutYet.UserNotLoggedInException;
import com.example.carparkwhere.ModelObjects.Carpark;
import com.example.carparkwhere.FilesIdkWhereToPutYet.NetworkCallEventListener;
import com.example.carparkwhere.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class DetailCarparkActivity extends AppCompatActivity {
    private TextView parkingRates_TV, carparkNumber_TV, carparkAddress_TV, timeAdvice_TV, averageRating_TV, totalReviews_TV, liveAvailaibility_TV, capacity_TV;
    private ImageButton bookmarkToggle_IMGBTN, submitReview_IMGBTN, backDetailCarparkActivity_IMGBTN, tutorial_IMGBTN, detailDirection_IMGBTN;
    public RatingBar averageRatingInStars;
    private ProgressDialog nDialog;
    private BarChart barChart;
    private Spinner spinner;
    private ArrayList<String> userBookmarkCarparks;
    private boolean userBookmarkedThis = false;
    private CarparkDao carparkDaoHelper;
    private ReviewDao reviewDaoHelper;
    private BookmarkDao bookmarkDaoHelper;
    private UserDataDao userDataDaoHelper;

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

        int day = 0;
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
        }catch (UserNotLoggedInException e){

        }


        // Getting the current Availability
        carparkDaoHelper.getCarparkLiveAvailability(getIntent().getStringExtra("CARPARK_ID"), new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                if (isSuccessful){
                    Integer liveAvailability = (Integer) networkCallResult;
                    System.out.println(liveAvailability);
                    liveAvailaibility_TV.setText(liveAvailability + "");
                }
            }
        });

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
                }catch (UserNotLoggedInException e){

                }

            }
        });

        // With server interface manager get average ratings of the carpark and display it
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

        // Get the directions to the carpark from the current location
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
      
        // Get the number of reviews for the specific carpark
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
        totalReviews_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailCarparkActivity.this, CarparkReviewsActivity.class);
                intent.putExtra("carparkid",getIntent().getStringExtra("CARPARK_ID"));
                startActivity(intent);
            }
        });

        //  With ServerInterfaceManager, get the carpark detail from the carpark details server.
        Intent intent = getIntent();
        final String str = intent.getStringExtra("CARPARK_ID"); // Get the carpark number
        carparkDaoHelper.getCarparkDetailsByID(str, new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                nDialog.dismiss();
                Carpark carpark = (Carpark) networkCallResult;
                carparkNumber_TV.setText(carpark.carparkNo);
                carparkAddress_TV.setText(carpark.carparkName);
                capacity_TV.setText(Integer.toString(carpark.carDetails.capacity));
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
        final int finalDay = day;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int difference = i - finalDay;
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

        timeAdvice_TV.setText(getIntent().getStringExtra("Year"));

        //carparkDaoHelper.getCarparkAvailabilityPredictionByDateTime(str, )
    }

    //   Function for Progress bar to load carpark detail
    private void presentProgressDialog(String message){
        nDialog = new ProgressDialog(DetailCarparkActivity.this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle(message);
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }

    // Function to identify the day of the week
    private int identifyDay (){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        return calendar.get(Calendar.DAY_OF_WEEK) % 7-2;
    }

    // Function to open up the help dialogue
    public void openDialog() {
        Dialog help = new Dialog(DetailCarparkActivity.this);
        help.requestWindowFeature(Window.FEATURE_NO_TITLE);
        help.setContentView(R.layout.detail_carpark_tutorial);
        help.show();
    }

    // With ServerInterfaceManager, get the predicted number of carpark lots.
    private void getAvailabilityPredictionData(Integer increment, String carparkNumber){
        carparkDaoHelper.getCarparkWholeDayPredictedAvailability(carparkNumber, increment, new Response.Listener() {
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

        }, error -> error.printStackTrace());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

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