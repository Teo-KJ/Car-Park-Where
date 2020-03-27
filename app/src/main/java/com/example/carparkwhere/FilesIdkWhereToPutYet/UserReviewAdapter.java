package com.example.carparkwhere.FilesIdkWhereToPutYet;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carparkwhere.ModelObjects.Review;
import com.example.carparkwhere.R;

import java.util.List;

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.ViewHolder> {

    List<Review> userReviews;
    Context context;
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
        //this changes the text
        Review user_reviews = userReviews.get(position);
        holder.textView.setText(user_reviews.getCarparkId());
        holder.userComments.setText(user_reviews.getComment()); //here need get the user comments
        //holder.userRating.setRating(user_reviews.getRating()); //here need find number of stars need change Rating to float
    }
    //number of items in the view
    @Override
    public int getItemCount() {
        return userReviews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView userComments;
        RatingBar userRating;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userComments = itemView.findViewById(R.id.userReview);
            textView = itemView.findViewById(R.id.carpark_id);
            userRating = itemView.findViewById(R.id.ratingBar);
        }

//         userComments.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View view){
//                   Review review = userReviews.get(getAdapterPosition());
//                   //reviewDAOhelper.getCarparkDetailsByID(review.get_id(), new NetworkCallEventListener() {
//                        @Override
//                        public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
//                            Review review = (Review) networkCallResult;
//                            Review review = userReviews.get(getAdapterPosition());
//                            //String text = bookmark.getCarparkID();
//                            //System.out.println("Info "+text);
//                            Intent intent = new Intent(context, SubmitReviewActivity.class);
//                            intent.putExtra();
//                            intent.putExtra();
//                            intent.putExtra();
//                            context.startActivity(intent);
//                        }
//                    });


    }

}