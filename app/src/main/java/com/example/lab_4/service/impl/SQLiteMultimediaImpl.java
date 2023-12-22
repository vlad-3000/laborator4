package com.example.lab_4.service.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lab_4.service.CommonService;
import com.example.lab_4.service.MultimediaRepository;
import com.example.lab_4.service.MultimediaSQLiteHelper;
import com.example.lab_4.service.models.SongModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SQLiteMultimediaImpl implements MultimediaRepository {
    private final String TABLE_NAME = "multimedia";
    private final String ID = "ID";
    private final String MUSICIAN = "musician";
    private final String NAME = "name";
    private final String TIME_INSERT = "time_insert";
    private MultimediaSQLiteHelper mHelper;

    public SQLiteMultimediaImpl(SQLiteOpenHelper helper) {
        this.mHelper = (MultimediaSQLiteHelper) helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT NOT NULL, %s TEXT NOT NULL, %s INTEGER NOT NULL, UNIQUE(%s,%s))",
                    TABLE_NAME,
                    ID,
                    MUSICIAN,
                    NAME,
                    TIME_INSERT,
                    MUSICIAN, NAME));
        } catch (SQLException e) {
            e.printStackTrace();
            CommonService.getInstance().showToast(e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(String.format("DROP TABLE IF EXISTS %S", TABLE_NAME));
            onCreate(db);
        } catch (SQLException e) {
            CommonService.getInstance().showToast(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public long updateOrInsertSong(String musician, String name) {
        ContentValues values = new ContentValues();
        values.put(MUSICIAN, musician);
        values.put(NAME, name);
        values.put(TIME_INSERT, Calendar.getInstance().getTime().getTime());
        try {
            long id = mHelper.getWritableDatabase().insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            CommonService.getInstance().showToast(e.getMessage());
            return -1;
        }
    }

    @Override
    public List<SongModel> getSongs() {
        List<SongModel> data = new ArrayList<>();
        try {
            Cursor cursor = mHelper.getReadableDatabase().query(TABLE_NAME, new String[]{ID, MUSICIAN, NAME, TIME_INSERT}, null, null, null, null, TIME_INSERT);
            if (cursor == null) return null;
            if (!cursor.moveToFirst()) return null;
            String musucian, name;
            Long id;
            long time_insert;
            do {
                id = cursor.getLong(0);
                musucian = cursor.getString(1);
                name = cursor.getString(2);
                time_insert = cursor.getLong(3);
                data.add(new SongModel(id, musucian, name, time_insert));
            } while (cursor.moveToNext());
        } catch (RuntimeException e) {
            e.printStackTrace();
            CommonService.getInstance().showToast(e.getMessage());
        }
        return data;
    }
}
