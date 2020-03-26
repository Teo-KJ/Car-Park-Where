package com.example.carparkwhere.FilesIdkWhereToPutYet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carparkwhere.Activities.DetailCarparkActivity;
import com.example.carparkwhere.Activities.SubmitReviewActivity;
import com.example.carparkwhere.Activities.UserBookmarksActivity;
import com.example.carparkwhere.DAO.DAOImplementations.CarparkDaoImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.CarparkDao;
import com.example.carparkwhere.ModelObjects.BookmarkedCarpark;
import com.example.carparkwhere.ModelObjects.Carpark;
import com.example.carparkwhere.R;

import java.util.List;

public class BookmarkAdaptor extends RecyclerView.Adapter<BookmarkAdaptor.ViewHolder> {

    List<BookmarkedCarpark> bookmarkedCarparks;
    Context context;
    CarparkDao carparkDaoHelper;

    public BookmarkAdaptor(List<BookmarkedCarpark> bookmarkedCarparks, Context context) {
        this.bookmarkedCarparks = bookmarkedCarparks;
        this.context = context;
        carparkDaoHelper = new CarparkDaoImpl(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.bookmark_carparks, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //this changes the text
        BookmarkedCarpark bookmarks = bookmarkedCarparks.get(position);
        holder.textView.setText(bookmarks.getCarparkID());
        boolean isExpanded = bookmarkedCarparks.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }
    //number of items in the view
    @Override
    public int getItemCount() {
        return bookmarkedCarparks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageButton direct,info;
        ConstraintLayout expandableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.titleTextView);
            direct = itemView.findViewById(R.id.btnDirection);
            info = itemView.findViewById(R.id.btnInformation);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);

            textView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    BookmarkedCarpark bookmark = bookmarkedCarparks.get(getAdapterPosition());
                    bookmark.setExpanded(!bookmark.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
            direct.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    BookmarkedCarpark bookmark = bookmarkedCarparks.get(getAdapterPosition());
                    String text = bookmark.getCarparkID();
                    System.out.println("Directions to "+text);

                    carparkDaoHelper.getCarparkDetailsByID(bookmark.getCarparkID(), new NetworkCallEventListener() {
                        @Override
                        public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                            Carpark carpark = (Carpark) networkCallResult;
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + carpark.latitude + "," + carpark.longitude);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            context.startActivity(mapIntent);
                        }
                    });
                }
            });
            info.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    BookmarkedCarpark bookmark = bookmarkedCarparks.get(getAdapterPosition());
                    carparkDaoHelper.getCarparkDetailsByID(bookmark.getCarparkID(), new NetworkCallEventListener() {
                        @Override
                        public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                            Carpark carpark = (Carpark) networkCallResult;
                            BookmarkedCarpark bookmark = bookmarkedCarparks.get(getAdapterPosition());
                            String text = bookmark.getCarparkID();
                            System.out.println("Info "+text);
                            Intent intent = new Intent(context, DetailCarparkActivity.class);
                            intent.putExtra("CARPARK_ID",bookmark.getCarparkID());
                            intent.putExtra("Lat", carpark.latitude);
                            intent.putExtra("Lng", carpark.longitude);
                            context.startActivity(intent);
                        }
                    });

                }
            });
        }
    }
}
