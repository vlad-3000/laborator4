package com.example.lab_4.service.models;

public class SongModel {
    public final long id;
    public final String musician;
    public final String name;
    public final long timeInsert;

    public SongModel(long id, String musician, String name, long timeInsert) {
        this.id = id;
        this.musician = musician;
        this.name = name;
        this.timeInsert = timeInsert;
    }
}
