package com.mient.mimusicplayer.model;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by mircea.ionita on 12/5/2016.
 */

public class Track implements Serializable {

    private long trackId;
    private long audioId;
    private String title;
    private String artist;
    private long albumId;
    private String album;
    private long duration;
    private Uri uri;
    private String coverArtPath;
    private boolean favorite;

    public Track(long trackId, long audioId, String title, String artist, long albumId, String album, long duration, Uri uri, String coverArtPath) {
        this.trackId = trackId;
        this.audioId = audioId;
        this.title = title;
        this.artist = artist;
        this.albumId = albumId;
        this.album = album;
        this.duration = duration;
        this.uri = uri;
        this.coverArtPath = coverArtPath;
        this.favorite = false;
    }

    public long getTrackId() {
        return trackId;
    }

    public long getAudioId() {
        return audioId;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public long getAlbumId() {
        return albumId;
    }

    public String getAlbum() {
        return album;
    }

    public long getDuration() {
        return duration;
    }

    public Uri getUri() {
        return uri;
    }

    public String getCoverArtPath() {
        return coverArtPath;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
