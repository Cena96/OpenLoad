package com.example.ashut.openload;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DbHelperDatabase extends SQLiteOpenHelper {

    private static final String MOVIE_TABLE = "MovieTable";


    DbHelperDatabase(@Nullable Context context) {
        super(context,
                "MovieTable", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table "
                + MOVIE_TABLE
                + "(Name text,Genre text,Year text,DownloadLink text,ImageUrl text,Description text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + MOVIE_TABLE);
        onCreate(db);

    }
}
