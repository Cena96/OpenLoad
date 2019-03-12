package com.example.ashut.openload;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@SuppressWarnings("ConstantConditions")
public class DbProvider extends ContentProvider {


    public static final String PROVIDER = "com.example.ashut.openload.DbProvider";
    public static final String AUTHORITY = "content://" + PROVIDER;

    public static final String Table_Movie = "MovieTable";

    private SQLiteDatabase db;
    @Override
    public boolean onCreate() {

        DbHelperDatabase dbHelperDatabase = new DbHelperDatabase(getContext());
        db = dbHelperDatabase.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return db.query(Table_Movie, projection, selection, selectionArgs,
                null, null, sortOrder);

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long insertedLong = db.insert(Table_Movie, "", values);
        return Uri.parse(AUTHORITY + "/" + Table_Movie + "/" + insertedLong);
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

}
