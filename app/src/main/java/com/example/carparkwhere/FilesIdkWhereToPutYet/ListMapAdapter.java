package com.example.carparkwhere.FilesIdkWhereToPutYet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.carparkwhere.Activities.DetailCarparkActivity;
import com.example.carparkwhere.Activities.MapsActivity;
import com.example.carparkwhere.DAO.DAOImplementations.CarparkDaoImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.CarparkDao;
import com.example.carparkwhere.ModelObjects.BookmarkedCarpark;
import com.example.carparkwhere.ModelObjects.Carpark;
import com.example.carparkwhere.R;
import com.google.api.Distribution;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ListMapAdapter extends RecyclerView.Adapter<ListMapAdapter.ViewHolder> {

    List<BookmarkedCarpark> bookmarkedCarparks;
    Context context;
    CarparkDao carparkDaoHelper;
    private UserListRecyclerClickListener mClickListener;


    public ListMapAdapter(List<BookmarkedCarpark> bookmarkedCarparks, Context context, UserListRecyclerClickListener clickListener) {
        this.bookmarkedCarparks = bookmarkedCarparks;
        this.context = context;
        carparkDaoHelper = new CarparkDaoImpl(context);
        mClickListener=clickListener;
    }
    public interface UserListRecyclerClickListener{
        void onUserClicked(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.bookmark_carparks, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mClickListener);
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView,address_TV,liveAvailability_bookmark_TV;
        ImageButton direct,info;
        ConstraintLayout expandableLayout;
        UserListRecyclerClickListener mClickListener;
        LinearLayout topLinearLayout;

        public ViewHolder(@NonNull View itemView, UserListRecyclerClickListener clickListener) {
            super(itemView);

            textView = itemView.findViewById(R.id.titleTextView);
            direct = itemView.findViewById(R.id.btnDirection);
            info = itemView.findViewById(R.id.btnInformation);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            topLinearLayout = itemView.findViewById(R.id.topLinearLayout);
            address_TV = itemView.findViewById(R.id.carparkAddress_TV);
            liveAvailability_bookmark_TV = itemView.findViewById(R.id.liveAvailability_bookmark_TV);
            mClickListener = clickListener;
            itemView.setOnClickListener(this);
            topLinearLayout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    BookmarkedCarpark bookmark = bookmarkedCarparks.get(getAdapterPosition());
                    bookmark.setExpanded(!bookmark.isExpanded());
                    mClickListener.onUserClicked(getAdapterPosition());
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

        @Override
        public void onClick(View view) {
            mClickListener.onUserClicked(getAdapterPosition());
        }
    }
}
