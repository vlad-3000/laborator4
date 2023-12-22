package com.example.lab_4.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MultimediaSQLiteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "lab4_db";
    private static final int DB_VERSION = 8;
    public final String TABLE_NAME = "multimedia";

    public MultimediaSQLiteHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Service.getInstance().multimediaRepository.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Service.getInstance().multimediaRepository.onUpgrade(db, oldVersion, newVersion);
    }
}
