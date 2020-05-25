package com.example.cst2335_finalproject.GuardianNews;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cst2335_finalproject.R;

import java.util.ArrayList;

public class FavoriteGuardianNewsctivity extends AppCompatActivity {

    GuardianNewsDBHelper dbHelper;
    SQLiteDatabase database;
    ArrayList<GuardianNews> favNewsList;
    ListView favNewsListView;
    FavNewsAdapter favNewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_guardian_newsctivity);

        favNewsList = new ArrayList<>();
        favNewsListView = findViewById(R.id.favGuardianNewsListView);

        //get db help which will send query to retrieve data form database
        dbHelper = new GuardianNewsDBHelper(getApplicationContext(),GuardianNewsDBHelper.DB_NAME,null,GuardianNewsDBHelper.DEFAULT_VERSION);
        database = dbHelper.getReadableDatabase();

        //send query to database
        Cursor cursor = database.query
                (GuardianNewsDBHelper.TABLE_FAVORITE_GUARDIAN_NEWS,null,null,null,null,null,null);
        //record col index of different column
        int idIndex = cursor.getColumnIndex(GuardianNewsDBHelper.ID);
        int titleIndex = cursor.getColumnIndex(GuardianNewsDBHelper.TITLE);
        int sectionIndex = cursor.getColumnIndex(GuardianNewsDBHelper.SECTION_NAME);
        int urlIndex = cursor.getColumnIndex(GuardianNewsDBHelper.URL);

        //loop through each row of data by moving the cursor to next row
        //if no more data, moveToNext() will return false and finish the loop
        while(cursor.moveToNext()){
            //get databaseid, title, sectionName and url of each record
            //then store it in a GuardianNews object
            long databaseId = cursor.getLong(idIndex);
            String title = cursor.getString(titleIndex);
            String sectionName = cursor.getString(sectionIndex);
            String newsUrl = cursor.getString(urlIndex);
            GuardianNews favNews = new GuardianNews();
            favNews.setWebUrl(newsUrl);
            favNews.setWebTitle(title);
            favNews.setSectionName(sectionName);
            favNews.setDatabaseId(databaseId);

            //put the news obj to arraylist which will then become the datasource of adapte
            //via the adapter, news will be diplayed on listview
            favNewsList.add(favNews);
        }

        if(favNewsList.size()>0) {
            favNewsAdapter = new FavNewsAdapter();
            favNewsListView.setAdapter(favNewsAdapter);
        }else{
            Toast.makeText(getApplicationContext(),"No news saved",Toast.LENGTH_LONG).show();
        }
    }

    public void updateFavoriteList(){
        favNewsAdapter.notifyDataSetChanged();
    }

    private class FavNewsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return favNewsList.size();
        }

        @Override
        public GuardianNews getItem(int position) {
            return favNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return favNewsList.get(position).getDatabaseId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.fav_guardian_news_item,null);
                TextView newsTitleTextView = convertView.findViewById(R.id.favGuardianNewsTitle);
                TextView newsUrlTextView = convertView.findViewById(R.id.favGuardianNewsUrl);
                GuardianNews gnews = (GuardianNews)getItem(position);
                newsTitleTextView.setText(gnews.getWebTitle());
                newsUrlTextView.setText(gnews.getWebUrl());

                Button deleteBtn = convertView.findViewById(R.id.deleteFavGuardianNews);
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long databaseId = gnews.getDatabaseId();
                        Toast.makeText(getApplicationContext(),"Delete a news : "+databaseId,Toast.LENGTH_SHORT).show();
                        int id = database.delete
                                (GuardianNewsDBHelper.TABLE_FAVORITE_GUARDIAN_NEWS, GuardianNewsDBHelper.ID+" = ?", new String[]{databaseId+""});
                        if( id != 0 ){
                            Toast.makeText(getApplicationContext(),"Delete successfully",Toast.LENGTH_SHORT).show();
                            favNewsList.remove(position);
                            updateFavoriteList();
                        }else{
                            Toast.makeText(getApplicationContext(),"Delete operation failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            return convertView;
        }
    }
}
