package com.example.cst2335_finalproject.GuardianNews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.util.LinkifyCompat;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cst2335_finalproject.R;

public class GuardianNewsDetailActivity extends AppCompatActivity {

    TextView titleView;
    TextView urlView;
    TextView sectionNameView;
    Button likeThisNews;
    GuardianNewsDBHelper dbHelper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_news_detail);

        titleView = findViewById(R.id.showGnewsTitle);
        urlView = findViewById(R.id.showGnewsUrl);
        sectionNameView = findViewById(R.id.showGnewsSectionName);
        // get news detail information
        GuardianNews gnews = (GuardianNews) getIntent().getSerializableExtra(GuardianNewActivity.NEWS_DETAIL);

        titleView.setText(gnews.getWebTitle());
        urlView.setText(gnews.getWebUrl());
        //urlView.setAutoLinkMask(Linkify.WEB_URLS);
        sectionNameView.setText(gnews.getSectionName());

        dbHelper = new GuardianNewsDBHelper(getApplicationContext(),GuardianNewsDBHelper.DB_NAME,null,GuardianNewsDBHelper.DEFAULT_VERSION);
        db = dbHelper.getWritableDatabase();

        likeThisNews = findViewById(R.id.saveNewBtn);
        likeThisNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv = new ContentValues();
                cv.put(GuardianNewsDBHelper.TITLE,gnews.getWebTitle());
                cv.put(GuardianNewsDBHelper.URL,gnews.getWebUrl());
                cv.put(GuardianNewsDBHelper.SECTION_NAME,gnews.getSectionName());
                Long id = db.insert(GuardianNewsDBHelper.TABLE_FAVORITE_GUARDIAN_NEWS,null,cv);

                if(id == -1){
                    Toast.makeText(getApplicationContext(),"Failed to save this news",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Successfully save this news",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
