package com.example.musicplayer;

public class Song {

    private final String title;
    private final String titleURI;
    private final String length;

    public Song(String title, String rawID,  String length) {
        this.title = title;
        this.titleURI = rawID;
        this.length = length;
    }

    public String getTitleURI() {
        return titleURI;
    }

    public String getTitle() {
        return title;
    }

    public String getLength() {
        return length;
    }
}
