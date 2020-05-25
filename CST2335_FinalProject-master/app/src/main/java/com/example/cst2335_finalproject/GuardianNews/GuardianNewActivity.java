package com.example.cst2335_finalproject.GuardianNews;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import com.example.cst2335_finalproject.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

    public class GuardianNewActivity extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar pb;
    Button searchBtn;
    ListView newsList;
    EditText searchText;
    ArrayList<GuardianNews> news;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    FrameLayout detailFrame;
    boolean isTablet;


    public static final String NEWS_DETAIL = "news_detail";
    public static final String SEARCH_HISTORY_FILE = "search_history";
    public static final String SEARCH_HISTORY_SAVE_TAG = "search_history_save_tag";
    public static final String EMPTY_SEARCH_HISTORY = "empty_search_history";
    public static final String NEWS_TITLE = "news_title";
    public static final String NEWS_Link = "news_link";
    public static final String NEWS_SECTION_NAME = "news_section_name";
    /**
     * This is function for setting a snackbar to go back to previous page
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            Snackbar sbar = Snackbar.make(toolbar,"Wanna go back?",Snackbar.LENGTH_INDEFINITE);
            sbar.setAction("Back",(e)->{
                Toast.makeText(GuardianNewActivity.this,"Going Back ...",Toast.LENGTH_SHORT).show();
                finish();
            });
            sbar.show();
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardian_new);

        news = new ArrayList<>();
        newsList = findViewById(R.id.lv_list);

        //get sharepref and save last search in it
        sharedPreferences = getSharedPreferences(SEARCH_HISTORY_FILE, Context.MODE_PRIVATE);
        editor =sharedPreferences.edit();
        String searchHistory = sharedPreferences.getString(SEARCH_HISTORY_SAVE_TAG,EMPTY_SEARCH_HISTORY);

        //get framelayout
        detailFrame = findViewById(R.id.news_detail_fragment);
        isTablet = detailFrame!=null;

        //set a customized tool bar
        toolbar = findViewById(R.id.tb_bar);
        setSupportActionBar(toolbar);

        //get edit text for search
        searchText = findViewById(R.id.et_search);

        if(searchHistory != null && !searchHistory.equalsIgnoreCase(EMPTY_SEARCH_HISTORY)){
            searchText.setHint("Last search : "+searchHistory);
        }

        //get search button and set an onclick event to it
        searchBtn = findViewById(R.id.btn_search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = searchText.getText().toString();

                if(search!=null && !search.trim().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(),"Search news "+search +" ...",Toast.LENGTH_LONG).show();
                    editor.putString(SEARCH_HISTORY_SAVE_TAG,search);
                    editor.commit();
                    NewsLoader newsLoader = new NewsLoader(search);
                    newsLoader.execute();
                }
            }
        });

        pb = findViewById(R.id.guardianNewPb);
        pb.setVisibility(ProgressBar.INVISIBLE);
    }

    /**
     * set icons to toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_help,menu);
        return true;
    }

    /**
     * set onclick event to icons on toolbar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.help){
            String msg = "This is an instruction";
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(msg).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create().show();
        }else if( itemId == R.id.favGNews){

            Intent intent = new Intent(GuardianNewActivity.this,FavoriteGuardianNewsctivity.class);
            startActivity(intent);

        }

        return true;
    }

    //This is a class for loading information from 3rd API
    private class NewsLoader extends AsyncTask {
        public static final String URL_BASE = "https://content.guardianapis.com/search?api-key=1fb36b70-1588-4259-b703-2570ea1fac6a&q=";
        private String search;

        public NewsLoader(String search){
            this.search = search;
        }

        /**
         * read data from 3rd api in backend
         * @param objects
         * @return
         */
        @Override
        protected Object doInBackground(Object[] objects) {
            String res = "";
            String searchUrl = URL_BASE+search;
            try {
                URL url = new URL(searchUrl);
                HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlCon.getInputStream();
                parseGuadianNewsJSON(inputStream);
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPostExecute(Object o) {
            MyAdapter adapter = new MyAdapter(news,GuardianNewActivity.this);
            newsList.setAdapter(adapter);

            //set onclick event for items in listview
            newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GuardianNews gnews = news.get(position);
                    if(isTablet){
                        GuardianNewsDetailFragment gFragment = new GuardianNewsDetailFragment();
                        Bundle dataToPass = new Bundle();
                        dataToPass.putString(NEWS_TITLE,gnews.getWebTitle());
                        dataToPass.putString(NEWS_Link,gnews.getWebUrl());
                        dataToPass.putString(NEWS_SECTION_NAME,gnews.getSectionName());
                        gFragment.setArguments(dataToPass);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                        ft.add(R.id.news_detail_fragment,gFragment);
                        ft.replace(R.id.news_detail_fragment,gFragment);
                        ft.commit();

                    }else {
                        Intent i = new Intent(GuardianNewActivity.this, GuardianNewsDetailActivity.class);
                        i.putExtra(NEWS_DETAIL, gnews);
                        startActivity(i);
                    }
                }
            });
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        /**
         * parse JSON data from inputstream to java object and put them to news
         * @param inputStream
         */
        private void parseGuadianNewsJSON(InputStream inputStream){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder builder = new StringBuilder();
                String line = null;

                while ((line = reader.readLine())!=null){
                    builder.append(line);
                }

                String jsonResultString = builder.toString();
                JSONObject jObj = new JSONObject(jsonResultString);
                JSONObject resultObj = jObj.getJSONObject("response");
                if(resultObj != null && resultObj.getString("status").equalsIgnoreCase("ok")){
                    JSONArray jArray = resultObj.getJSONArray("results");

                    for(int i=0; i<jArray.length();i++){
                        JSONObject newsJson = jArray.getJSONObject(i);
                        if(newsJson!=null){
                            String id = newsJson.getString("id");
                            String type = newsJson.getString("type");
                            String sectionId = newsJson.getString("sectionId");
                            String sectionName = newsJson.getString("sectionName");
                            String webPublicationDate = newsJson.getString("webPublicationDate");
                            String webTitle = newsJson.getString("webTitle");
                            String webUrl = newsJson.getString("webUrl");
                            String apiUrl = newsJson.getString("apiUrl");
                            String isHosted = newsJson.getString("isHosted");
                            String pillarId = newsJson.getString("pillarId");
                            String pillarName = newsJson.getString("pillarName");

                            GuardianNews gnews = new GuardianNews();
                            gnews.setId(id);
                            gnews.setType(type);
                            gnews.setSectionId(sectionId);
                            gnews.setSectionName(sectionName);
                            gnews.setWebPublicationDate(webPublicationDate);
                            gnews.setWebTitle(webTitle);
                            gnews.setWebUrl(webUrl);
                            gnews.setApiUrl(apiUrl);
                            gnews.setIsHosted(isHosted);
                            gnews.setPillarId(pillarId);
                            gnews.setPillarName(pillarName);

                            news.add(gnews);
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
