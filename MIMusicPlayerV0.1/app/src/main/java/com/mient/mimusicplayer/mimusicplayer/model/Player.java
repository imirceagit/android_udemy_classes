package com.mient.mimusicplayer.mimusicplayer.model;

import java.util.ArrayList;

/**
 * Created by mircea.ionita on 12/5/2016.
 */

public class Player {

    private static Player instance;

    private ArrayList<Track> playingList;
    private int playingTrackPosition;
    private Track playingTrack;
    private int playerState;
    private int shuffleMode;
    private int repeatMode;

    public static Player getInstance(){
        if(instance == null){
            instance = new Player();
        }
        return instance;
    }

    public Player(){

    }

    public void initPlayer(ArrayList<Track> list, int position){
        playingList = list;
        playingTrackPosition = position;
    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public int getPlayerState() {
        return playerState;
    }

    public int getShuffleMode() {
        return shuffleMode;
    }
}
