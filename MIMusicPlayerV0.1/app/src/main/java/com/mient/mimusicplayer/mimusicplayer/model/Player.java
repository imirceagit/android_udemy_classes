package com.mient.mimusicplayer.mimusicplayer.model;

import android.content.Intent;

import com.mient.mimusicplayer.mimusicplayer.activities.MainActivity;
import com.mient.mimusicplayer.mimusicplayer.services.ForegroundService;
import com.mient.mimusicplayer.mimusicplayer.services.MediaPlayerService;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mircea.ionita on 12/6/2016.
 */

public class Player {

    private ArrayList<Track> playingList;
    private int currentPlayingPosition;
    private Track currentPlayingTrack;
    private int progress;
    private int playerState;
    private int shuffleMode;
    private int repeatMode;

    private Track lastPlayedTrack;

    private MediaPlayerService mediaPlayerService;

    private MainActivity activity = MainActivity.mainActivity;

    public void initPlayer(ArrayList<Track> list, int position, int shuffle, int repeat){
        mediaPlayerService = new MediaPlayerService(playingList, currentPlayingPosition);
        playingList = list;
        currentPlayingPosition = position;
        setCurrentPlayingTrack();
        progress = 0;
        playerState = Constants.PLAYER_STATE.STOP;
        shuffleMode = shuffle;
        repeatMode = repeat;
    }

    public void restartPlayer(ArrayList<Track> list, int position){
        playingList = list;
        currentPlayingPosition = position;
        setCurrentPlayingTrack();
        progress = 0;
        play();
    }

    public void play(){
        playerState = Constants.PLAYER_STATE.PLAY;
        Intent playIntent = new Intent(activity, ForegroundService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        playIntent.putExtra(Constants.KEYS.PLAY_TRACK, currentPlayingPosition);
        activity.startService(playIntent);
        activity.updatePlayerUI();
    }

    public void pause(){
        playerState = Constants.PLAYER_STATE.PAUSE;
        Intent playIntent = new Intent(activity, ForegroundService.class);
        playIntent.setAction(Constants.ACTION.PAUSE_ACTION);
        activity.startService(playIntent);
        activity.updatePlayerUI();
    }

    public void stop(){
        playerState = Constants.PLAYER_STATE.STOP;
        Intent playIntent = new Intent(activity, ForegroundService.class);
        playIntent.setAction(Constants.ACTION.STOP_ACTION);
        activity.startService(playIntent);
        activity.updatePlayerUI();
    }

    public void prev(){
        Random r = new Random();
        if(shuffleMode == Constants.SHUFFLE_MODE.ON){
            int rand = r.nextInt(playingList.size());
            currentPlayingPosition = rand;
        }else if(currentPlayingPosition > 0 ){
            currentPlayingPosition--;
        }else if (currentPlayingPosition == 0){
            currentPlayingPosition = playingList.size() - 1;
        }
        setCurrentPlayingTrack();
        play();
    }

    public void next(){
        Random r = new Random();
        if(shuffleMode == Constants.SHUFFLE_MODE.ON){
            int rand = r.nextInt(playingList.size());
            currentPlayingPosition = rand;
        }else if(currentPlayingPosition < playingList.size() - 1){
            currentPlayingPosition++;
        }else if (currentPlayingPosition == playingList.size() - 1){
            currentPlayingPosition = 0;
        }
        setCurrentPlayingTrack();
        play();
    }

    public void shuffle(){
        if (shuffleMode == Constants.SHUFFLE_MODE.ON){
            shuffleMode = Constants.SHUFFLE_MODE.OFF;
        }else {
            shuffleMode = Constants.SHUFFLE_MODE.ON;
            repeatMode = Constants.REPEATE_MODE.OFF;
        }
        activity.updatePlayerUI();
    }

    public void repeat(){
        switch (repeatMode){
            case Constants.REPEATE_MODE.ALL:
                repeatMode = Constants.REPEATE_MODE.ONE;
                shuffleMode = Constants.SHUFFLE_MODE.OFF;
                break;
            case Constants.REPEATE_MODE.ONE:
                repeatMode = Constants.REPEATE_MODE.OFF;
                break;
            case Constants.REPEATE_MODE.OFF:
                repeatMode = Constants.REPEATE_MODE.ALL;
                shuffleMode = Constants.SHUFFLE_MODE.OFF;
                break;
            default:
                repeatMode = Constants.REPEATE_MODE.OFF;
        }
        activity.updatePlayerUI();
    }

    public void seek(int progress){
        Intent playIntent = new Intent(activity, ForegroundService.class);
        playIntent.setAction(Constants.ACTION.SEEK_ACTION);
        playIntent.putExtra(Constants.KEYS.SEEK_KEY, progress);
        activity.startService(playIntent);
        activity.updatePlayerUI();
    }

    public void setPlayingList(ArrayList<Track> playingList) {
        this.playingList = playingList;
    }

    public void setCurrentPlayingPosition(int currentPlayingPosition) {
        this.currentPlayingPosition = currentPlayingPosition;
    }

    public void setCurrentPlayingTrack(Track currentPlayingTrack) {
        this.currentPlayingTrack = currentPlayingTrack;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setPlayerState(int playerState) {
        this.playerState = playerState;
    }

    public void setShuffleMode(int shuffleMode) {
        this.shuffleMode = shuffleMode;
    }

    public void setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
    }

    public int getCurrentPlayingPosition() {
        return currentPlayingPosition;
    }

    public long getTrackDuration(){
        return currentPlayingTrack.getDuration();
    }

    public void setCurrentPlayingTrack() {
        currentPlayingTrack = playingList.get(currentPlayingPosition);
    }

    public Track getCurrentPlayingTrack() {
        return currentPlayingTrack;
    }

    public int getProgress() {
        return progress;
    }

    public int getPlayerState() {
        return playerState;
    }

    public int getShuffleMode() {
        return shuffleMode;
    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public void onDestroy() {
        mediaPlayerService.onDestroy();
    }
}
