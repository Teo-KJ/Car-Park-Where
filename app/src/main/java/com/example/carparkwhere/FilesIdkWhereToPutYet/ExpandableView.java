package com.example.carparkwhere.FilesIdkWhereToPutYet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

import com.example.carparkwhere.ModelObjects.BookmarkedCarpark;
import com.example.carparkwhere.R;
import java.util.ArrayList;
import java.util.List;

public class ExpandableView extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    List<BookmarkedCarpark> bookmarkedCarparks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_view);

        recyclerView = findViewById(R.id.recyclerView);
        // setting LinearLayout
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //this also can be done in XML
        //Making sure divider nice nice
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        //initialising data
        bookmarkedCarparks = new ArrayList<>();
        bookmarkedCarparks.add(new BookmarkedCarpark("A81"));
        bookmarkedCarparks.add(new BookmarkedCarpark("NTU North Spine"));
        bookmarkedCarparks.add(new BookmarkedCarpark("NIE"));
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerAdapter = new RecyclerAdapter(bookmarkedCarparks);
        recyclerView.setAdapter(recyclerAdapter);
    }
}
