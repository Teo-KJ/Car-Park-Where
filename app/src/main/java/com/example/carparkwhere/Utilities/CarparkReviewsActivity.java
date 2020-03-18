package com.example.carparkwhere.Utilities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.carparkwhere.R;

public class CarparkReviewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carpark_reviews);

        ImageButton backCarparkReviewsActivity_IMGBTN = findViewById(R.id.backCarparkReviewsActivity_IMGBTN);
        backCarparkReviewsActivity_IMGBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
