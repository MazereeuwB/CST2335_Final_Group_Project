package com.example.cst2335_finalproject.NASA_Imagery;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database class which stores the values given in a database to be accessed at a later time throughout the application
 */
public class MyOpener extends SQLiteOpenHelper {
    /**
     * database name
     */
    protected final static String DATABASE_NAME = "NASA_DB";
    /**
     * database version number
     */
    protected final static int VERSION_NUM = 1;
    /**
     * table name
     */
    public final static String TABLE_NAME = "NASA_Images";
    /**
     * column to store latitude data
     */
    public final static String COL_LAT = "Latitude";
    /**
     * column to store longitude data
     */
    public final static String COL_LONG = "Longitude";
    /**
     * column to store direction data
     */
    public final static String COL_DIRECTION = "Direction";
    /**
     * column to store image data
     */
    public final static String COL_IMAGE = "Image";
    /**
     * column to store id data
     */
    public final static String COL_ID = "_id";
    /**
     * create table sql statement
     */
    public final static String CREATE_TABLE  = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_LAT + " REAL, "
            + COL_LONG + " REAL,"
            + COL_DIRECTION + " TEXT,"
            + COL_IMAGE + " BLOB);";

    /**
     * constructor
     * @param ctx context
     */
    public MyOpener(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);

    }

    /**
     * constructs the database table
     * @param db database object
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    /**
     * if the database needs to be updated
     * @param db dabase object
     * @param oldVersion old version
     * @param newVersion new version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    /**
     * used if the user needs an older version of the database
     * @param db database object
     * @param oldVersion old version
     * @param newVersion new version
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }


}
