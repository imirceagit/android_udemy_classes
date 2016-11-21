package com.mient.mimusicplayer.mimusicplayer.model;

import android.net.Uri;

/**
 * Created by mircea.ionita on 11/21/2016.
 */

public class Song {

    private long id;
    private String title;
    private String artist;
    private String album;
    private String time;
    private Uri uri;
    private boolean favorite;

    public Song(long id, String title, String artist, String album, String time, Uri uri, boolean favorite) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.time = time;
        this.uri = uri;
        this.favorite = favorite;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getTime() {
        return time;
    }

    public Uri getUri() {
        return uri;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
