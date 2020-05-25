package com.example.cst2335_finalproject.bbc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Utility class to access database
 * called by most activities
 *
 * @author Jaewoo Kim
 */
public class BBC_Utils {
    /**
     * A SQLiteDatabase storing handle database
     */
    static SQLiteDatabase db;

    /**
     * Create db handle and store in a static variable
     * @param ctxt
     */
    static void start(Context ctxt) {
        BBC_Database dbOpener = new BBC_Database(ctxt);
        db = dbOpener.getWritableDatabase();
    }

    /**
     * Load stored articles from database
     */
    static void loadDataFromDatabase(ArrayList myList)
    {
        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = { BBC_Database.COL_ID, BBC_Database.COL_TITLE, BBC_Database.COL_DESC, BBC_Database.COL_URL, BBC_Database.COL_DATE };
        //query all the results from the database:
        Cursor results = db.query(false, BBC_Database.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int idColIndex = results.getColumnIndex(BBC_Database.COL_ID);
        int titleColIndex = results.getColumnIndex(BBC_Database.COL_TITLE);
        int descColumnIndex = results.getColumnIndex(BBC_Database.COL_DESC);
        int urlColumnIndex = results.getColumnIndex(BBC_Database.COL_URL);
        int dateColumnIndex = results.getColumnIndex(BBC_Database.COL_DATE);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            long id = results.getLong(idColIndex);
            String title = results.getString(titleColIndex);
            String desc = results.getString(descColumnIndex);
            String url = results.getString(urlColumnIndex);
            String date = results.getString(dateColumnIndex);

            myList.add(new BBC_Articles(id, title, desc, url, date));
        }
        results.moveToFirst();
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
    static long addArticleIntoDatabase(String title, String desc, String url, String date)
    {
        ContentValues newRowValues = new ContentValues();

        //Now provide a value for every database column defined in MyOpener.java:
        newRowValues.put(BBC_Database.COL_TITLE, title);
        newRowValues.put(BBC_Database.COL_DESC, desc);
        newRowValues.put(BBC_Database.COL_URL, url);
        newRowValues.put(BBC_Database.COL_DATE, date);

        //Now insert in the database:
        return db.insert(BBC_Database.TABLE_NAME, null, newRowValues);
    }

    /**
     * Delete the article
     *
     * @param id the Long having the position to be deleted
     * @return the Boolean which is true if it is deleted
     */
    static boolean deleteArticleInDatabase(long id)
    {
        if( db.delete(BBC_Database.TABLE_NAME, BBC_Database.COL_ID + "= ?", new String[] {Long.toString(id)})  == -1 )
            return false;

        return true;
    }

    /**
     * Check the article exists
     *
     * @param title the String having title to search
     * @return the Boolean which is true if it exists
     */
    boolean isExistArticle(String title)
    {
        Cursor results = db.query(false, BBC_Database.TABLE_NAME, new String[]{BBC_Database.COL_ID}, BBC_Database.COL_TITLE+" = ?", new String[]{title}, null, null, null, null);

        return (results.getCount() > 0);
    }

    /**
     * Search articles based on the query
     *
     * @param search the text for searching
     * @return the Integer which has a size of items found
     */
    static int searchDataFromDatabase(ArrayList myList, String search)
    {
        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = { BBC_Database.COL_ID, BBC_Database.COL_TITLE, BBC_Database.COL_DESC, BBC_Database.COL_URL, BBC_Database.COL_DATE };
        //query all the results from the database:
        Cursor results = db.query(false, BBC_Database.TABLE_NAME, columns, BBC_Database.COL_TITLE+" like ?", new String[]{"%"+search+"%"}, null, null, null, null);

        myList.clear();
        //Now the results object has rows of results that match the query.
        //find the column indices:
        int idColIndex = results.getColumnIndex(BBC_Database.COL_ID);
        int titleColIndex = results.getColumnIndex(BBC_Database.COL_TITLE);
        int descColumnIndex = results.getColumnIndex(BBC_Database.COL_DESC);
        int urlColumnIndex = results.getColumnIndex(BBC_Database.COL_URL);
        int dateColumnIndex = results.getColumnIndex(BBC_Database.COL_DATE);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            long id = results.getLong(idColIndex);
            String title = results.getString(titleColIndex);
            String desc = results.getString(descColumnIndex);
            String url = results.getString(urlColumnIndex);
            String date = results.getString(dateColumnIndex);

            myList.add(new BBC_Articles(id, title, desc, url, date));
        }
        return myList.size();
    }
}
