package com.example.cst2335_finalproject.NASA_Search;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class nasaDB extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "NasaImagesDB";
    private final static int VERSION_NUM = 1;
    protected final static String TABLE_NAME = "NasaImagesTable";
    protected final static String COL_IMAGE = "Image";
    protected final static String COL_TITLE = "TITLE";
    protected final static String COL_EXPLANATION = "EXPLANATION";
    protected final static String COL_DATE = "DATE";
    protected final static String COL_HDURL = "HDURL";


    public nasaDB(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_IMAGE + " IMAGE,"
                + COL_TITLE + " TEXT,"
                + COL_EXPLANATION + " TEXT,"
                + COL_DATE + " TEXT,"
                + COL_HDURL + " TEXT"
                + ");";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

