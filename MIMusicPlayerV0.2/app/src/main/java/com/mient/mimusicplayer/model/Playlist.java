package com.mient.mimusicplayer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by mircea.ionita on 2/16/2017.
 */

public class Playlist implements Serializable {

    private String name;
    private List<Track> trackList;
    private int trackCount;

    public Playlist() {
        this.name = "default";
        this.trackList = new ArrayList<Track>();
        this.trackCount = 0;
    }

    public Playlist(String name) {
        this.name = name;
        this.trackList = new ArrayList<Track>();
        this.trackCount = 0;
    }

    public Playlist(String name, List<Track> trackList) {
        this.name = name;
        this.trackList = trackList;
        this.trackCount = trackList.size();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
        this.trackCount = trackList.size();
    }

    public int getTrackCount() {
        return trackCount;
    }
}
