package com.example.carparkwhere.FilesIdkWhereToPutYet;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carparkwhere.ModelObjects.BookmarkedCarpark;
import com.example.carparkwhere.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static final String TAG = "RecyclerAdapter";
    int count = 0; //just for tutorial
    List<BookmarkedCarpark> bookmarkedCarparks;

    public RecyclerAdapter(List<BookmarkedCarpark> bookmarkedCarparks) {
        this.bookmarkedCarparks = bookmarkedCarparks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.i(TAG, "onCreateViewHolder: " + count++);
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

    class ViewHolder extends RecyclerView.ViewHolder { //MovieVH
        //ImageView imageView;
        TextView textView;
        ConstraintLayout expandableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.titleTextView);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);

            textView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    BookmarkedCarpark bookmark = bookmarkedCarparks.get(getAdapterPosition());
                    bookmark.setExpanded(!bookmark.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }

}