package com.example.carparkwhere;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.health.SystemHealthManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.carparkwhere.Models.RegisteredUser;
import com.example.carparkwhere.Models.Review;
import com.example.carparkwhere.Utilities.CarparkReviewsActivity;
import com.example.carparkwhere.Utilities.CarparkReviewsDataManager;
import com.example.carparkwhere.Utilities.FirebaseManager;
import com.example.carparkwhere.Utilities.NetworkCallEventListener;
import com.example.carparkwhere.Utilities.ServerInterfaceManager;
import com.example.carparkwhere.Utilities.UserDataManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Map;

import io.grpc.Server;

public class SignInActivity extends AppCompatActivity {
    EditText emailAddressEditText;
    EditText passwordEditText;
    Button signInButton;
    Button createAccountButton;
    Button guessModeButton;
    LottieAnimationView animationView;
    ProgressDialog nDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

//        checkIfUserIsAlreadySignIn();
        setupFindViewsByID();
        setupSignInButton();
        setupCreateAccountButton();
        setupAnimationView();
        setupGuessModeButton();

//        UserDataManager.getFavouriteCarparksId(new OnCompleteListener() {
//            @Override
//            public void onComplete(@NonNull Task task) {
//                System.out.println("hehe");
//                Map<String,ArrayList<String>> listOfKeysMap = (Map<String,ArrayList<String>>) task.getResult();
//                ArrayList<String> listOfKeys = listOfKeysMap.get("carparks");
//                System.out.println(listOfKeys);
//            }
//        });

        Button testBTN1 = findViewById(R.id.testBTN1);
        testBTN1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, CarparkReviewsActivity.class));
            }
        });



    }

    private void checkIfUserIsAlreadySignIn(){
        FirebaseUser user = FirebaseManager.getCurrentUser();
        boolean isEmailVerified = user.isEmailVerified();

        if ((user != null) && (isEmailVerified)){
            Toast.makeText(SignInActivity.this,"Successfully Signed In As " + user.getEmail(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignInActivity.this, MapsActivity.class);
            SignInActivity.this.startActivity ( intent );
        }
    }

    private void presentProgressDialog(String message){
        nDialog = new ProgressDialog(SignInActivity.this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle(message);
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
    }

    private void setupFindViewsByID(){
        emailAddressEditText = findViewById(R.id.emailAddressSignInEditText);
        passwordEditText = findViewById(R.id.passwordSignInEditText);
        signInButton = findViewById(R.id.signInButton);
        createAccountButton = findViewById(R.id.createAccountButton);
        guessModeButton = findViewById(R.id.guessModeButton);
    }

    private void setupAnimationView(){
        animationView = findViewById(R.id.signInMainLottieAnimation);
        animationView.setAnimationFromUrl ("https://assets5.lottiefiles.com/packages/lf20_u3YlGl.json");
    }

    private void setupSignInButton(){
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((emailAddressEditText.getText().toString().isEmpty() || passwordEditText.getText().toString().isEmpty())){
                    Toast.makeText(SignInActivity.this,"Email and Password cannot be blank. Try again", Toast.LENGTH_SHORT).show();
                }else{
                    presentProgressDialog("Verifying Credentials...");
                    FirebaseManager.signInWithEmail(SignInActivity.this, emailAddressEditText.getText().toString(), passwordEditText.getText().toString(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            nDialog.dismiss();
                            if (task.isSuccessful()){
                                if (FirebaseManager.getCurrentUser().isEmailVerified()){

                                    Toast.makeText(SignInActivity.this,"Successfully signed in!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignInActivity.this, MapsActivity.class);
                                    SignInActivity.this.startActivity ( intent );
                                    finish();
                                }else{
                                    Toast.makeText(SignInActivity.this,"You havent verified your email!", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(SignInActivity.this,"Credentials are wrong. try again!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }

    private void setupCreateAccountButton(){
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    private void setupGuessModeButton(){

        guessModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presentProgressDialog("Loading");
                Intent i = new Intent(SignInActivity.this, MapsActivity.class);
                startActivity(i);
                finish();
                nDialog.dismiss();
            }
        });
    }























    private void someexamples(){

///       1) Save New Carpark Review for a carpark;
//        2) Get Carpark Reviews by Carpark ID;
//        3) Get Carpark Reviews by User Email;
//        4) Update Existing Review with New Values;
//        5) Get Carpark Average Rating;
//        6) Delete Carpark Review;


        //1) save new carpark review
        Review newReview = new Review("kohsweesen@gmail.com",4.5,"A81","This carpark is perfect","Swee Sen");
        ServerInterfaceManager.saveNewCarparkReview(this, newReview, new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                if (isSuccessful){
                    //maybe show a success message
                }else{
                    //show some error message
                    System.out.println(errorMessage);
                }
            }
        });

        //2) Get carpark reviews by ID
        ServerInterfaceManager.getCarparkReviewsByCarparkID(this, "A81", new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                if (isSuccessful){
                    //we are casting the network call result into ArrayList<Review>
                    //the specific type to be casted down to for each function is stated in the server interface manager class above each function
                    ArrayList<Review> reviews = (ArrayList<Review>) networkCallResult;

                    //do something with the reviews
                    System.out.println(reviews.get(0).getUserDisplayName());
                }
                else{
                    System.out.println(errorMessage);
                }
            }
        });

        //3)Get Carpark by user
        ServerInterfaceManager.getCarparkReviewsByUserEmail(this, "kohsweesen99@gmail.com", new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                if (isSuccessful){
                    ArrayList<Review> reviews = (ArrayList<Review>) networkCallResult;
                    //do something with the reviews

                }else{
                    //do some error handling message
                }
            }
        });


        //4) Update exising review
        //Each review has a unique id named _id. We must reference this id to update the existing reviews on the database

        ServerInterfaceManager.updateCarparkReviewWithNewValues(this, "5e72eed54f34515956099894", new Review("kohsweesen99@gmail.com", 3.0, "A81", "This carpark is pretty bad", "Swee Sen"), new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                //in this case we are not trying to get any return value from the server so dont need to use networkcallresult
                if (isSuccessful){
                    //maybe toast some success message
                }else{
                    //maybe ask the user to try again
                }
            }
        });


        //5) Get carpark average rating
        ServerInterfaceManager.getCarparkAverageRating(this, "A81", new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                if (isSuccessful){
                    //in this case the return value as defined is Double, so just cast down to Double
                    Double averagerating = ((Double) networkCallResult);
                }
            }
        });


        //6) Delete carpark review
        //note again we need to get the review id
        //so u must input the id based on the reviews that u got: review.get_id()
        ServerInterfaceManager.deleteCarparkReviewByReviewID(this, "5e72eed54f34515956099894", new NetworkCallEventListener() {
            @Override
            public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                //again we are not expecting any retunr value from the network call result,
                if (isSuccessful){
                    //show some success message
                }else{
                    //show some error message
                }
            }
        });

    }












}
