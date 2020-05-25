package com.example.cst2335_finalproject.bbc;

import com.example.cst2335_finalproject.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Displays detailed information of given article
 * Has the button to open web browser
 *
 * @author Jaewoo Kim
 */
public class BBC_ArticleDetailActivity extends AppCompatActivity {
    private String detail_title;
    private String detail_description;
    private String detail_date;
    private String detail_link;

    /**
     * Loads data from previous activity and displays them
     * Construct the activity page
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbc_article_detail);

        Toolbar tBar = findViewById(R.id.bbc_toolbar);
        tBar.setTitle(getString(R.string.bbc_articleDetail_title));

        setSupportActionBar(tBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tBar.setNavigationOnClickListener(view -> {
            this.onBackPressed();
        });

        Intent myIntent = getIntent();

        detail_title = myIntent.getStringExtra("title");
        detail_description = myIntent.getStringExtra("description");
        detail_date = myIntent.getStringExtra("date");
        detail_link = myIntent.getStringExtra("link");

        ((TextView)findViewById(R.id.bbc_detail_title)).setText(detail_title);
        ((TextView)findViewById(R.id.bbc_detail_description)).setText(detail_description);
        ((TextView)findViewById(R.id.bbc_detail_date)).setText(detail_date);
        ((TextView)findViewById(R.id.bbc_detail_link)).setText(detail_link);

        findViewById(R.id.bbc_btn_goToLink).setOnClickListener( v -> {
            Intent nextPage = new Intent(Intent.ACTION_VIEW);
            nextPage.setData(Uri.parse(((TextView)findViewById(R.id.bbc_detail_link)).getText().toString()));

            startActivity(nextPage);
        });
        findViewById(R.id.bbc_btn_AddToFavorite).setOnClickListener( v -> {
            long id = BBC_Utils.addArticleIntoDatabase(detail_title, detail_description, detail_link, detail_date);

            Toast.makeText(BBC_ArticleDetailActivity.this,
                    id > 0 ? getString(R.string.bbc_add_succeed) : getString(R.string.bbc_add_fail),
                    Toast.LENGTH_SHORT).show();
        });
     }
}
