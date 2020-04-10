package com.example.carparkwhere.Adaptors;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carparkwhere.Activities.DetailCarparkActivity;
import com.example.carparkwhere.DAO.DAOImplementations.CarparkDaoImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.CarparkDao;
import com.example.carparkwhere.Interfaces.NetworkCallEventListener;
import com.example.carparkwhere.Entities.BookmarkedCarpark;
import com.example.carparkwhere.Entities.Carpark;
import com.example.carparkwhere.R;

import java.util.ArrayList;
import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    List<BookmarkedCarpark> bookmarkedCarparks;
//    List<Carpark> carparks;
    ArrayList<String> availabilityStrings;
    Context context;
    CarparkDao carparkDaoHelper;

    public BookmarkAdapter(List<BookmarkedCarpark> bookmarkedCarparks, Context context) {
        this.bookmarkedCarparks = bookmarkedCarparks;
//        this.carparks = carparks;
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
        holder.address_TV.setText(bookmarkedCarparks.get(position).getCarparkName());
        String availabilityString = bookmarkedCarparks.get(position).getAvailability();
        if (availabilityString.equals("")){
            availabilityString = "-";
        }
        holder.liveAvailability_bookmark_TV.setText(availabilityString);
    }
    //number of items in the view
    @Override
    public int getItemCount() {
        return bookmarkedCarparks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView,address_TV,liveAvailability_bookmark_TV;
        ImageButton direct,info;
        ConstraintLayout expandableLayout;
        LinearLayout topLinearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.titleTextView);
            direct = itemView.findViewById(R.id.btnDirection);
            info = itemView.findViewById(R.id.btnInformation);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            topLinearLayout = itemView.findViewById(R.id.topLinearLayout);
            address_TV = itemView.findViewById(R.id.carparkAddress_TV);
            liveAvailability_bookmark_TV = itemView.findViewById(R.id.liveAvailability_bookmark_TV);


            topLinearLayout.setOnClickListener(new View.OnClickListener(){
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