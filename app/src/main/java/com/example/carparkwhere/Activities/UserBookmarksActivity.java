package com.example.carparkwhere.Activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carparkwhere.DAO.DAOImplementations.BookmarkDaoImpl;
import com.example.carparkwhere.DAO.DAOImplementations.CarparkDaoImpl;
import com.example.carparkwhere.DAO.DAOImplementations.UserDataDaoFirebaseImpl;
import com.example.carparkwhere.DAO.DAOInterfaces.BookmarkDao;
import com.example.carparkwhere.DAO.DAOInterfaces.CarparkDao;
import com.example.carparkwhere.DAO.DAOInterfaces.UserDataDao;
import com.example.carparkwhere.FilesIdkWhereToPutYet.BookmarkAdaptor;
import com.example.carparkwhere.FilesIdkWhereToPutYet.NetworkCallEventListener;
import com.example.carparkwhere.FilesIdkWhereToPutYet.UserNotLoggedInException;
import com.example.carparkwhere.ModelObjects.BookmarkedCarpark;
import com.example.carparkwhere.R;

import java.util.ArrayList;
import java.util.List;

public class UserBookmarksActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    BookmarkAdaptor recyclerAdapter;
    List<String> retrievefromFirebase;
    List<BookmarkedCarpark> bookmarkedCarparks;
    BookmarkDao bookmarkDaoHelper;
    UserDataDao userDataDaoHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_bookmarks);

        bookmarkDaoHelper = new BookmarkDaoImpl(this);
        userDataDaoHelper = new UserDataDaoFirebaseImpl();

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#111111")));
        bar.setDisplayHomeAsUpEnabled(true);
        try{
            bar.setTitle("Bookmarks for " + userDataDaoHelper.getDisplayName());
        }catch (UserNotLoggedInException e){
            e.printStackTrace();
        }


        recyclerView = findViewById(R.id.recyclerView);
        // setting LinearLayout
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); //this also can be done in XML
        //Making sure divider nice nice
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        //initialising data
        retrievefromFirebase = new ArrayList<>();
        //from this retrieve from firestore, add bookmark_carparks into bookmarkedCarparks
        //constructor of bookmark_carparks is just takes in the carpark_id
        //once done then can remove bottom hardcode
        bookmarkedCarparks = new ArrayList<>();

        try{
            bookmarkDaoHelper.getUserBookmarkCarparkIds(userDataDaoHelper.getUserEmail(), new NetworkCallEventListener() {
                @Override
                public <T> void onComplete(T networkCallResult, Boolean isSuccessful, String errorMessage) {
                    if (isSuccessful){
                        bookmarkedCarparks = new ArrayList<>();
                        ArrayList<String> carparkIds = (ArrayList<String>) networkCallResult;
                        for (String carparkId:carparkIds){
                            bookmarkedCarparks.add(new BookmarkedCarpark(carparkId));
                        }
                        initRecyclerView();
                    }
                }
            });
        }catch (UserNotLoggedInException e){
            e.printStackTrace();
        }


//        bookmarkedCarparks.add(new BookmarkedCarpark("A81"));
//        bookmarkedCarparks.add(new BookmarkedCarpark("NTU North Spine"));
//        bookmarkedCarparks.add(new BookmarkedCarpark("NIE"));
//        initRecyclerView();
    }



    private void initRecyclerView() {
        recyclerAdapter = new BookmarkAdaptor(bookmarkedCarparks,this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}