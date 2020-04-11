package com.example.carparkwhere.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carparkwhere.Activities.SubmitReviewActivity;
import com.example.carparkwhere.DAO.DAOImplementations.UserDataDaoFirebaseImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.UserDataDao;
import com.example.carparkwhere.Exceptions.UserNotLoggedInException;
import com.example.carparkwhere.Entities.Review;
import com.example.carparkwhere.R;

import java.util.ArrayList;

/*
 * This class implements the CarparkReviewsAdapter. This is used to adapt the given input data to the RecyclerView.
 * This class uses the item_carparkreviews.xml for the user interface.
 *
 * @author Tay Jaslyn
 * */
public class CarparkReviewsAdapter extends
        RecyclerView.Adapter<CarparkReviewsAdapter.ViewHolder> {

    UserDataDao userDataDaoHelper;
    String userEmail;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        userDataDaoHelper = new UserDataDaoFirebaseImpl();
        try{
            userEmail = userDataDaoHelper.getUserEmail();
        }catch (UserNotLoggedInException e){
            userEmail = " ";
        }

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

        TextView date_TV = viewHolder.date_TV;
        date_TV.setText(review.getDateString());

        if (!userEmail.equals(review.getUserEmail())){
            viewHolder.editButton_BT.setVisibility(View.INVISIBLE);
        }

        viewHolder.editButton_BT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SubmitReviewActivity.class);
                intent.putExtra("carparkid",review.getCarparkId());
                view.getContext().startActivity(intent);
            }
        });


        Log.d(onBind_TitleTV.getText().toString(),"onBind title");
        Log.d(onBind_ContentTV.getText().toString(),"onBind content");
        Log.d(Integer.toString(onBind_RatingRBAR.getNumStars()),"onBind rating");

    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    //Linking user interface of each row in the RecyclerView.
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemTitle_TV;
        private RatingBar itemRating_RBAR;
        private TextView itemContent_TV,date_TV;
        private ImageButton editButton_BT;

        public ViewHolder(View itemView) {
            super(itemView);
           itemTitle_TV = (TextView) itemView.findViewById(R.id.itemTitle_TV);
           itemRating_RBAR = (RatingBar) itemView.findViewById(R.id.itemRating_RBAR);
           itemContent_TV = (TextView) itemView.findViewById(R.id.itemContent_TV);
           date_TV = itemView.findViewById(R.id.date_TV);
           editButton_BT = itemView.findViewById(R.id.editButton_BT);

        }
    }

    private ArrayList<Review> mReviews;
    public CarparkReviewsAdapter(ArrayList<Review> reviews){
        mReviews = reviews;
    }

}
