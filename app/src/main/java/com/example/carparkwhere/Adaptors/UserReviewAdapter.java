package com.example.carparkwhere.Adaptors;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carparkwhere.Activities.SubmitReviewActivity;
import com.example.carparkwhere.Entities.Review;
import com.example.carparkwhere.R;

import java.util.List;
/*
 * This class implements the UserReviewAdapter. This is used to adapt the given input data to the RecyclerView.
 * This class uses the user_reviews.xml for the user interface.
 * @author Pang Kia Le Jordon, Koh Swee Sen
 * */
public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.ViewHolder> {

    List<Review> userReviews;
    Context context;
    //UserReviewAdapter will be used for UserReviewsActivity
    public UserReviewAdapter(List<Review> userReviews){
        this.userReviews = userReviews;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_reviews, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //this changes the text and rating bar of the view
        Review user_reviews = userReviews.get(position);
        holder.textView.setText(user_reviews.getCarparkId());
        holder.carparkName.setText(user_reviews.getCarparkName());
        holder.userComments.setText(user_reviews.getComment()); 
        holder.userRating.setRating(user_reviews.getRating().floatValue()); 
        holder.reviewDate.setText(user_reviews.getDateString());

        //Onclick listener to direct the user to SubmitReviewActivity for user to update their review
        holder.baseLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("as","Going to submit review page");
                Intent intent = new Intent(view.getContext(), SubmitReviewActivity.class);
                intent.putExtra("carparkid",user_reviews.getCarparkId());
                view.getContext().startActivity(intent);
            }
        });
    }
    //number of items in the view, determines number of recycler view created
    @Override
    public int getItemCount() {
        return userReviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView userComments;
        TextView reviewDate;
        TextView carparkName;
        RatingBar userRating;
        LinearLayout baseLinearLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userComments = itemView.findViewById(R.id.userReview);
            textView = itemView.findViewById(R.id.carpark_id);
            userRating = itemView.findViewById(R.id.ratingBar);
            reviewDate = itemView.findViewById(R.id.reviewDate);
            carparkName = itemView.findViewById(R.id.carpark_name);
            baseLinearLayout = itemView.findViewById(R.id.baseLinearLayout);
        }


    }

}