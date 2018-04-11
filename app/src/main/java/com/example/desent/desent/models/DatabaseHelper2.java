package com.example.desent.desent.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ragnhlar on 11.04.2018.
 */

public class DatabaseHelper2 extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NewDatabase.db";

    private static final String LOG = "DatabaseHelper2";

    private Context appContext;

    /*
    private float walkingDist = 0;
    private float cyclingDist = 0;
    private float drivingDist = 0;*/

    public DatabaseHelper2(Context context){
        super(context, DATABASE_NAME, null, 1);
        this.appContext = context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
