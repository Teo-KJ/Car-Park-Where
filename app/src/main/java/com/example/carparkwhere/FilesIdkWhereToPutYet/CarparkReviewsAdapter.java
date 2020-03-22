package com.example.carparkwhere.FilesIdkWhereToPutYet;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carparkwhere.ModelObjects.Review;
import com.example.carparkwhere.R;

import java.util.ArrayList;

public class CarparkReviewsAdapter extends
        RecyclerView.Adapter<CarparkReviewsAdapter.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View reviewView = inflater.inflate(R.layout.item_carparkreviews, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(reviewView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CarparkReviewsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Review review = mReviews.get(position);

        // Set item views based on your views and data model
        TextView onBind_TitleTV = viewHolder.itemTitle_TV;
        onBind_TitleTV.setText(review.getUserDisplayName());
        TextView onBind_ContentTV = viewHolder.itemContent_TV;
        onBind_ContentTV.setText(review.getComment());
        RatingBar onBind_RatingRBAR = viewHolder.itemRating_RBAR;
        onBind_RatingRBAR.setRating(review.getRating().floatValue());

        Log.d(onBind_TitleTV.getText().toString(),"onBind title");
        Log.d(onBind_ContentTV.getText().toString(),"onBind content");
        Log.d(Integer.toString(onBind_RatingRBAR.getNumStars()),"onBind rating");

    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemTitle_TV;
        private RatingBar itemRating_RBAR;
        private TextView itemContent_TV;

        public ViewHolder(View itemView) {
            super(itemView);
           itemTitle_TV = (TextView) itemView.findViewById(R.id.itemTitle_TV);
           itemRating_RBAR = (RatingBar) itemView.findViewById(R.id.itemRating_RBAR);
           itemContent_TV = (TextView) itemView.findViewById(R.id.itemContent_TV);

        }
    }

    private ArrayList<Review> mReviews;
    public CarparkReviewsAdapter(ArrayList<Review> reviews){
        mReviews = reviews;
    }

}
