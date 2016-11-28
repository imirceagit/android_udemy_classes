package com.mient.mimusicplayer.mimusicplayer.model;

import android.net.Uri;

/**
 * Created by mircea.ionita on 11/21/2016.
 */

public class Song {

    private long id;
    private long audioId;
    private String title;
    private String artist;
    private long albumId;
    private String album;
    private long time;
    private Uri uri;
    private boolean favorite;

    private String coverArtPath;

    public Song(long id, long audioId, String title, String artist, long albumId, String album, long time, Uri uri, boolean favorite) {
        this.id = id;
        this.audioId = audioId;
        this.title = title;
        this.artist = artist;
        this.albumId = albumId;
        this.album = album;
        this.time = time;
        this.uri = uri;
        this.favorite = favorite;
    }

    public Song(long id, long audioId, String title, String artist, long albumId, String album, String coverArtPath, long time, Uri uri, boolean favorite) {
        this.id = id;
        this.audioId = audioId;
        this.title = title;
        this.artist = artist;
        this.albumId = albumId;
        this.album = album;
        this.coverArtPath = coverArtPath;
        this.time = time;
        this.uri = uri;
        this.favorite = favorite;
    }

    public Song() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAudioId() { return audioId; }

    public void setAudioId(long audioId) { this.audioId = audioId; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getCoverArtPath() {
        return coverArtPath;
    }

    public void setCoverArtPath(String coverArtPath) {
        this.coverArtPath = coverArtPath;
    }
}
