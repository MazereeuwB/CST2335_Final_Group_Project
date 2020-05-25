package com.example.cst2335_finalproject.bbc;

import com.example.cst2335_finalproject.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * Fragment page of BBC News reader
 * Download articles from BBC and displays the list of articles
 *
 * @author Jaewoo Kim
 */
public class BBC_Fragment_ArticleList extends Fragment {
    static final String ACTIVITY_NAME = "BBC_F_ArticleList";
    /**
     * A ArrayList storing the list of articles to display on screen
     */
    ArrayList<BBC_Articles> myArticleList = new ArrayList<>();

    /**
     * A MyListAdapter extending BaseAdapter to display ListView
     */
    private MyListAdapter myAdapter;

    /**
     * A ProgressBar showing download progress when download articles from BBC server
     */
    private ProgressBar myProgressBar;

    /**
     * A AppCompatActivity keeping parentActivity value
     */
    private AppCompatActivity parentActivity;

    public BBC_Fragment_ArticleList() {
        // Required empty public constructor
    }

    /**
     * Initialize every information to display the list of articles from BBC
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the View which displays the list at the fragment of bbc main
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.activity_bbc_fragment_list, container, false);

        BBC_ArticleUpdate req = new BBC_ArticleUpdate();
        req.execute("http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");

        ((ListView)result.findViewById(R.id.list_article)).setAdapter(myAdapter = new MyListAdapter());
        ((ListView)result.findViewById(R.id.list_article)).setOnItemClickListener( (parent, view, position, id)->{
            Intent nextPage = new Intent( parentActivity, BBC_ArticleDetailActivity.class);

            nextPage.putExtra("title", myArticleList.get(position).getTitle() );
            nextPage.putExtra("description", myArticleList.get(position).getDescription() );
            nextPage.putExtra("date", myArticleList.get(position).getDate() );
            nextPage.putExtra("link", myArticleList.get(position).getLink() );
            nextPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            startActivity(nextPage);
        });

        myProgressBar = result.findViewById(R.id.bbc_progressBar);
        myProgressBar.setVisibility(View.VISIBLE);

        return result;
    }

    /**
     * Store parent's information
     *
     * @param context the Context which has parent's context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        parentActivity = (AppCompatActivity)context;
    }

    /**
     * Add articles into ArrayList
     * @param title the String which has title to be added
     * @param desc the String which has description to be added
     * @param url the String which has URL to be added
     * @param date the String which has date to be added
     * @return the Boolean which is true if it is added
     */
    boolean addArticle(String title, String desc, String url, String date)
    {
        return myArticleList.add(new BBC_Articles(title, desc, url, date));
    }

    /**
     * Add articles into ArrayList
     * @param list the ArrayList which has the list of articles
     * @return the Boolean which is true if it is added
     */
    boolean addArticle(ArrayList<BBC_Articles> list)
    {
        return myArticleList.addAll(list);
    }

    /**
     * Construct and display the Listview having articles
     */
    private class MyListAdapter extends BaseAdapter {
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            View newView = view;
            LayoutInflater inflater = getLayoutInflater();

            BBC_Articles CurrentRow = (BBC_Articles)getItem(position);

            newView = inflater.inflate(R.layout.bbc_article_row, parent, false);

            ((TextView)newView.findViewById(R.id.row_title)).setText(CurrentRow.getTitle());
            ((TextView)newView.findViewById(R.id.row_date)).setText(CurrentRow.getShortDate());

            return newView;
        }

        @Override
        public int getCount() {
            return myArticleList.size();
        }

        @Override
        public Object getItem(int i) {
            return myArticleList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }
    }

    /**
     * Download articles from BBC server at background
     * extending AsyncTask
     */
    private class BBC_ArticleUpdate extends AsyncTask<String, Integer, String> {
        String title;
        String description;
        String date;
        String link;
        ArrayList<BBC_Articles> retrievedList = new ArrayList<>();

        /**
         * Retrieve article data from BBC server in background
         *
         * @param strings the String which has URL to open
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {
            Log.d(ACTIVITY_NAME, "doInBackground : start");

            try {
                URL url = new URL(strings[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");

                String parameter = null;

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while(eventType != XmlPullParser.END_DOCUMENT)
                {
                    if(eventType == XmlPullParser.START_TAG)
                    {
                        if(xpp.getName().equals("item")) {
                            xpp.next();
                            xpp.next();
                            title = getText(xpp).trim();
                            xpp.next();
                            xpp.next();
                            description = getText(xpp).trim();
                            xpp.next();
                            xpp.next();
                            link = getText(xpp).trim();
                            xpp.next();
                            xpp.next();
                            xpp.next();
                            xpp.next();
                            xpp.next();
                            xpp.next();
                            date = getText(xpp).trim();

                            retrievedList.add(new BBC_Articles(title, description, link, date));
                            publishProgress(myArticleList.size()*100/40);
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }
                publishProgress(100);
            }
            catch (Exception e)
            {
                Log.e(ACTIVITY_NAME, e.getMessage());
            }
            return "Done";
        }

        /**
         * Update progress bar
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            myProgressBar.setProgress(values[0]);
        }

        /**
         * Request to add articles into Database
         * Shows the number of updated articles
         *
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d(ACTIVITY_NAME, "onPostExecute : Size of retrievedList"+retrievedList.size());

            if(addArticle(retrievedList)) {
                myAdapter.notifyDataSetChanged();
                Toast.makeText(parentActivity,
                        myArticleList.size() + " " + parentActivity.getString(R.string.bbc_articleupdated),
                        Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(parentActivity, parentActivity.getString(R.string.bbc_load_failed),
                        Toast.LENGTH_SHORT).show();

            //myProgressBar.setVisibility(View.INVISIBLE);
        }

        private String getText(XmlPullParser parser) throws IOException, XmlPullParserException {
            String retText;

            parser.next();
            retText = parser.getText();
            parser.next();

            return retText;
        }

        private void skip(XmlPullParser parser) throws IOException, XmlPullParserException {
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }
    }

}
