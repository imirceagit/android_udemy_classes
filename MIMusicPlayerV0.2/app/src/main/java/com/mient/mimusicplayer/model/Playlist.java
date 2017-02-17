package com.mient.mimusicplayer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mircea.ionita on 2/16/2017.
 */

public class Playlist {

    private String name;
    private List<Track> trackList;
    private int currentPosition;
    private int trackCount;

    public Playlist() {
        this.trackList = new ArrayList<Track>();
        this.currentPosition = 0;
        this.trackCount = 0;
    }

    public Playlist(String name) {
        this.name = name;
        this.trackList = new ArrayList<Track>();
        this.currentPosition = 0;
        this.trackCount = 0;
    }

    public Playlist(String name, List<Track> trackList) {
        this.name = name;
        this.trackList = trackList;
        this.currentPosition = 0;
        this.trackCount = trackList.size();
    }

    public Track getCurrentTrack(){
        return trackList.get(currentPosition);
    }

    public Track getNextTrack(){
        if (currentPosition == (trackCount - 1)){
            currentPosition = 0;
        }else {
            currentPosition++;
        }
        return trackList.get(currentPosition);
    }

    public Track getPrevTrack(){
        if (currentPosition == 0){
            currentPosition = (trackCount - 1);
        }else {
            currentPosition--;
        }
        return trackList.get(currentPosition);
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

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getTrackCount() {
        return trackCount;
    }
}
