package com.example.cst2335_finalproject.NASA_Search;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ListView;

import com.example.cst2335_finalproject.R;

import java.util.ArrayList;

public class Nasa_FavList extends AppCompatActivity {
    private nasaDB nasaOpener;
    private SQLiteDatabase db;
    private ArrayList<singleRow> list = new ArrayList<>();
    private Nasa_SearchPage.customAdapter adapter;
    private ListView favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa__fav_list);

        nasaOpener = new nasaDB(this);
        db = nasaOpener.getWritableDatabase();
        getViews();

        String[] columns = {nasaDB.COL_IMAGE, nasaDB.COL_TITLE, nasaDB.COL_EXPLANATION, nasaDB.COL_DATE, nasaDB.COL_HDURL};
        Cursor cursor = db.query(false, nasaDB.TABLE_NAME, columns, null, null, null, null, null, null);
        int imageIndex = cursor.getColumnIndex(nasaDB.COL_IMAGE);
        int titleIndex = cursor.getColumnIndex(nasaDB.COL_TITLE);
        int explanationIndex = cursor.getColumnIndex(nasaDB.COL_EXPLANATION);
        int dateIndex = cursor.getColumnIndex(nasaDB.COL_DATE);
        int hdLinkIndex = cursor.getColumnIndex(nasaDB.COL_HDURL);

        if (cursor.moveToFirst()) {
            do {
                list.add(new singleRow(null, cursor.getString(titleIndex), cursor.getString(explanationIndex), cursor.getString(dateIndex), cursor.getString(hdLinkIndex)));
            } while (cursor.moveToNext());

            adapter = new Nasa_SearchPage.customAdapter(this);
            adapter.notifyDataSetChanged();

            favoriteList = findViewById(R.id.favListView);
            favoriteList.setAdapter(adapter);

//            favoriteList.setOnItemClickListener((parent, view, position, id) -> {
//                Bundle dataToPass = new Bundle();
//                dataToPass.putString(ITEM_SECTION, items.get(position).getSectionName());
//                dataToPass.putString(ITEM_TITLE, items.get(position).getWebTitle());
//                dataToPass.putString(ITEM_URL, items.get(position).getWebUrl());
        }

    }
    private void getViews() {
        favoriteList = (ListView) findViewById(R.id.favListView);
    }
}