package com.example.cst2335_finalproject.GuardianNews;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//class to connect to databse
public class GuardianNewsDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_FAVORITE_GUARDIAN_NEWS = "fav_guardian_news";
    public static final String ID = "_id";
    public static final String TITLE = "gnew_title";
    public static final String URL = "gnews_url";
    public static final String SECTION_NAME = "gnews_sec_name";
    public static final int DEFAULT_VERSION = 1;
    public static final String DB_NAME = "Guardian_News_Database";

    public GuardianNewsDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =
                "CREATE TABLE " + TABLE_FAVORITE_GUARDIAN_NEWS
                        + "( "+ ID +" INTEGER \tPRIMARY KEY AUTOINCREMENT, "+ TITLE +" text, "
                        + URL +" text, " + SECTION_NAME + "\t text);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
