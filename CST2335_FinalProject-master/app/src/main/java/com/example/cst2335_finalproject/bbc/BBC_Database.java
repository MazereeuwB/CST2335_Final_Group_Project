package com.example.cst2335_finalproject.bbc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Create, upgrade and drop the database for BBC News Reader
 *
 * @author Jaewoo Kim
 */
public class BBC_Database extends SQLiteOpenHelper {
    /**
     * A static String which stores database name
     */
    protected final static String DATABASE_NAME = "BBC_Articles";

    /**
     * A static Integer which stores the version number of database
     */
    protected final static int VERSION_NUM = 2;

    /**
     * A static String which stores table name
     */
    public final static String TABLE_NAME = "BBCARTICLE_T";

    /**
     * A static String which stores the name of id column in database
     */
    public final static String COL_ID = "_id";

    /**
     * A static String which stores the name of title column in database
     */
    public final static String COL_TITLE = "TITLE";

    /**
     * A static String which stores the name of description column in database
     */
    public final static String COL_DESC = "DESCRIPTION";

    /**
     * A static String which stores the name of URL column in database
     */
    public final static String COL_URL = "URL";

    /**
     * A static String which stores the name of date column in database
     */
    public final static String COL_DATE = "DATE";

    /**
     * Connect to the database
     * @param ctx
     */
    public BBC_Database(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     * Create table if it doesn't exist
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT,"
                + COL_DESC + " TEXT,"
                + COL_URL + " TEXT UNIQUE,"
                + COL_DATE  + " TEXT);");
    }

    /**
     * Drop and recreate table if the version is updated
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_NAME);
        this.onCreate(db);
    }
}
