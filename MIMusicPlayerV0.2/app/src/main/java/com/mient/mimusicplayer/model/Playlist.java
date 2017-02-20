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

    public Track getNextTrack(boolean shuffle){
        if (shuffle){
            Random r = new Random();
            int rand = r.nextInt(trackCount);
            currentPosition = rand;
        } else{
            if (currentPosition == (trackCount - 1)){
                currentPosition = 0;
            }else {
                currentPosition = currentPosition + 1;
            }
        }

        return trackList.get(currentPosition);
    }

    public Track getPrevTrack(boolean shuffle){
        if (shuffle){
            Random r = new Random();
            int rand = r.nextInt(trackCount);
            currentPosition = rand;
        } else {
            if (currentPosition == 0){
                currentPosition = (trackCount - 1);
            }else {
                currentPosition = currentPosition - 1;
            }
        }
        return trackList.get(currentPosition);
    }

    public boolean isLastTrack(){
        if (currentPosition == trackCount - 1){
            return true;
        }else {
            return false;
        }
    }

    public void resetPlaylist(){
        setCurrentPosition(0);
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
