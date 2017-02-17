package com.mient.mimusicplayer.services;

import android.content.Intent;

import com.mient.mimusicplayer.activities.MainActivity;
import com.mient.mimusicplayer.model.Constants;
import com.mient.mimusicplayer.model.Track;

/**
 * Created by mircea.ionita on 2/17/2017.
 */

public class Player {

    private MainActivity activity = MainActivity.mainActivity;

    private Track currentTrack;
    private int progress;
    private int playerState;
    private int shuffleMode;
    private int repeatMode;

    public void play(int position){
        Intent playIntent = new Intent(activity, ForegroundService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        playIntent.putExtra(Constants.KEYS.PLAY_TRACK, position);
        activity.startService(playIntent);
        playerState = Constants.PLAYER_STATE.PLAY;
        activity.updatePlayerUI();
    }

    public void pause(){
        Intent pauseIntent = new Intent(activity, ForegroundService.class);
        pauseIntent.setAction(Constants.ACTION.PAUSE_ACTION);
        activity.startService(pauseIntent);
        playerState = Constants.PLAYER_STATE.PAUSE;
        activity.updatePlayerUI();
    }

    public void next(){
        Intent nextIntent = new Intent(activity, ForegroundService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        activity.startService(nextIntent);
        playerState = Constants.PLAYER_STATE.PLAY;
        activity.updatePlayerUI();
    }

    public void prev(){
        Intent prevIntent = new Intent(activity, ForegroundService.class);
        prevIntent.setAction(Constants.ACTION.PREV_ACTION);
        activity.startService(prevIntent);
        playerState = Constants.PLAYER_STATE.PLAY;
        activity.updatePlayerUI();
    }

    public void stop(){
        Intent stopIntent = new Intent(activity, ForegroundService.class);
        stopIntent.setAction(Constants.ACTION.STOP_ACTION);
        activity.startService(stopIntent);
        playerState = Constants.PLAYER_STATE.STOP;
        activity.updatePlayerUI();
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

        activity.updatePlayerUI();
    }

    public int getPlayerState() {
        return playerState;
    }

    public void setPlayerState(int playerState) {
        this.playerState = playerState;
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getShuffleMode() {
        return shuffleMode;
    }

    public void setShuffleMode(int shuffleMode) {
        this.shuffleMode = shuffleMode;
    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
    }
}
