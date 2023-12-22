package com.example.lab_4.service;

import android.database.sqlite.SQLiteDatabase;

import com.example.lab_4.service.models.SongModel;

import java.util.List;

public interface MultimediaRepository {
    void onCreate(SQLiteDatabase db);

    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    /**
     * Добавить или заменить песню в БД
     *
     * @param musician - музыкант
     * @param name     - наименование песни
     * @return id - идентификатор новой записи в БД или -1
     */
    long updateOrInsertSong(String musician, String name);

    /**
     * Получить все песни из БД
     *
     * @return - список SongModel
     */
    List<SongModel> getSongs();
}
