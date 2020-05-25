package com.example.cst2335_finalproject.bbc;

import com.example.cst2335_finalproject.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Displays the List of favorite articles
 * Loads data from database
 *
 * @author Jaewoo Kim
 */
public class BBC_Fragment_Favorites extends Fragment {
    /**
     * A ArrayList storing the list of articles to display on screen
     */
    ArrayList<BBC_Articles> myFavoritesList = new ArrayList<>();

    /**
     * A MyListAdapter extending BaseAdapter to display ListView
     */
    private MyListAdapter myAdapter;

    /**
     * A SharedPreferences storing search text
     */
    private SharedPreferences prefs;

    /**
     * A AppCompatActivity keeping parentActivity value
     */
    private AppCompatActivity parentActivity;

    public BBC_Fragment_Favorites() {
        // Required empty public constructor
    }

    /**
     * Initialize every information to display the list of favorites from Database
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the View which displays the list at the fragment of bbc main
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.activity_bbc_fragment_favorites, container, false);

        prefs = parentActivity.getSharedPreferences("BBC", Context.MODE_PRIVATE);
        String savedSearch = prefs.getString("BBC_SEARCH","");

        ((EditText)result.findViewById(R.id.bbc_edittext_search)).setText(savedSearch);

        //BBC_Utils.loadDataFromDatabase(myFavoritesList);
        BBC_Utils.searchDataFromDatabase(myFavoritesList, savedSearch);

        ((ListView)result.findViewById(R.id.list_favorites)).setAdapter(myAdapter = new MyListAdapter());
        ((ListView)result.findViewById(R.id.list_favorites)).setOnItemClickListener( (parent, view, position, id)->{
            Intent nextPage = new Intent( parentActivity, BBC_ArticleDetailActivity.class);

            nextPage.putExtra("title", myFavoritesList.get(position).getTitle() );
            nextPage.putExtra("description", myFavoritesList.get(position).getDescription() );
            nextPage.putExtra("date", myFavoritesList.get(position).getDate() );
            nextPage.putExtra("link", myFavoritesList.get(position).getLink() );
            nextPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            startActivity(nextPage);
        });

        ((ListView)result.findViewById(R.id.list_favorites)).setOnItemLongClickListener( (parent, view, position, id) -> {
            AlertDialog.Builder myBuilder = new AlertDialog.Builder(parentActivity);
            myBuilder.setTitle(getString(R.string.bbc_delete_alert_title));
            myBuilder.setMessage("ID : "+myFavoritesList.get(position).getId()+"\n"
                    +getString(R.string.bbc_title)+" : "+myFavoritesList.get(position).getTitle());
            myBuilder.setPositiveButton(getString(R.string.bbc_cm_yes), (click, arg) -> {
                BBC_Articles deleted = deleteArticle(position, myFavoritesList.get(position).getId());
                myAdapter.notifyDataSetChanged();

                Snackbar snackbar = Snackbar.make(parentActivity.getCurrentFocus(), getString(R.string.bbc_deleted), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.bbc_undo), v -> {
                            addArticle(deleted.getTitle(), deleted.getDescription(), deleted.getLink(), deleted.getDate());
                            myAdapter.notifyDataSetChanged();

                            Snackbar.make(parentActivity.getCurrentFocus(), getString(R.string.bbc_undo_done), Snackbar.LENGTH_LONG)
                                    .show();
                        });
                snackbar.show();
            });
            myBuilder.setNegativeButton(getString(R.string.bbc_cm_no), (click, arg) -> {});
            myBuilder.create().show();

            return true;
        });

        result.findViewById(R.id.bbc_button_search).setOnClickListener(view -> {
            int searched = BBC_Utils.searchDataFromDatabase(myFavoritesList, ((EditText)result.findViewById(R.id.bbc_edittext_search)).getText().toString());
            myAdapter.notifyDataSetChanged();

            Toast.makeText(parentActivity, searched+" "+getString(R.string.bbc_found), Toast.LENGTH_SHORT).show();
        });

        return result;
    }

    /**
     * Store SharedPreferences variable to a file
     */
    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences.Editor edit = prefs.edit();

        edit.putString("BBC_SEARCH", ((EditText)getView().findViewById(R.id.bbc_edittext_search)).getText().toString());
        edit.commit();
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
     * Add articles into both of ArrayList and database
     *
     * @param title the String which has title to be added
     * @param desc the String which has description to be added
     * @param url the String which has URL to be added
     * @param date the String which has date to be added
     * @return the Boolean which is true if it is added
     */
    boolean addArticle(String title, String desc, String url, String date)
    {
        long id =  BBC_Utils.addArticleIntoDatabase(title, desc, url, date);

        if( id == - 1 ) return false;

        myFavoritesList.add(new BBC_Articles(id, title, desc, url, date));
        return true;
    }

    /**
     * Delete the article
     *
     * @param position the Integer having the position to be deleted
     * @return the Boolean which is true if it is deleted
     */
    BBC_Articles deleteArticle(int position, long id)
    {
        if( BBC_Utils.deleteArticleInDatabase(id) == false )
            return null;

        return myFavoritesList.remove(position);
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
            return myFavoritesList.size();
        }

        @Override
        public Object getItem(int i) {
            return myFavoritesList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return myFavoritesList.get(i).getId();
        }
    }
}
