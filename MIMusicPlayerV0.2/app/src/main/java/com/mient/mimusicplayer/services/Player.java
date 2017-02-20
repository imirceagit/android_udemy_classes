package com.mient.mimusicplayer.services;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.mient.mimusicplayer.activities.MainActivity;
import com.mient.mimusicplayer.model.Constants;
import com.mient.mimusicplayer.model.Playlist;
import com.mient.mimusicplayer.model.Track;

/**
 * Created by mircea.ionita on 2/17/2017.
 */

public class Player {

    private MainActivity activity = MainActivity.mainActivity;

    private static final String LOG_TAG = "PLAYER";

    public static Playlist currentPlaylist;
    private int progress;
    private int playerState;
    private int shuffleMode;
    private int repeatMode;

    public void initPlayer(Playlist list, boolean onStart){
        currentPlaylist = list;
        Intent startIntent = new Intent(activity, ForegroundService.class);
        startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startIntent.putExtra(Constants.KEYS.SHUFFLE, shuffleMode);
        startIntent.putExtra(Constants.KEYS.REPEAT, repeatMode);
        startIntent.putExtra(Constants.KEYS.ONSTART, onStart);
        activity.startService(startIntent);

        if (!onStart){
            playerState = Constants.PLAYER_STATE.PLAY;
        }
        activity.updatePlayerUI();
    }

    public void play(){
        Intent playIntent = new Intent(activity, ForegroundService.class);
        if (playerState == Constants.PLAYER_STATE.PAUSE){
            playIntent.setAction(Constants.ACTION.RESUME_ACTION);
        } else {
            playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        }
        activity.startService(playIntent);
        playerState = Constants.PLAYER_STATE.PLAY;
        Log.v(LOG_TAG, "++++++++++++++++++PLAY+++++++++++++++++++++");
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

    public void destroy(){
        Intent destroyIntent = new Intent(activity, ForegroundService.class);
        destroyIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        activity.startService(destroyIntent);
    }

    public void shuffle(){
        if (shuffleMode == Constants.SHUFFLE_MODE.ON){
            shuffleMode = Constants.SHUFFLE_MODE.OFF;
        }else {
            shuffleMode = Constants.SHUFFLE_MODE.ON;
            repeatMode = Constants.REPEATE_MODE.OFF;
        }
        Intent upgradeIntent = new Intent(activity, ForegroundService.class);
        upgradeIntent.setAction(Constants.ACTION.UPGRADE_ACTION);
        upgradeIntent.putExtra(Constants.KEYS.SHUFFLE, shuffleMode);
        upgradeIntent.putExtra(Constants.KEYS.REPEAT, repeatMode);
        activity.startService(upgradeIntent);
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
        Intent upgradeIntent = new Intent(activity, ForegroundService.class);
        upgradeIntent.setAction(Constants.ACTION.UPGRADE_ACTION);
        upgradeIntent.putExtra(Constants.KEYS.SHUFFLE, shuffleMode);
        upgradeIntent.putExtra(Constants.KEYS.REPEAT, repeatMode);
        activity.startService(upgradeIntent);
        activity.updatePlayerUI();
    }

    public void seek(int progress){
        Log.v(LOG_TAG, " ======================= " + progress);
        Intent seekIntent = new Intent(activity, ForegroundService.class);
        seekIntent.setAction(Constants.ACTION.SEEK_ACTION);
        seekIntent.putExtra(Constants.KEYS.SEEK_KEY, progress);
        activity.startService(seekIntent);
        activity.updatePlayerUI();
    }

    public static void setCurrentPlaylist(Playlist currentPlaylist) {
        Player.currentPlaylist = currentPlaylist;
    }

    public void setCurrentTrack(int i){
        Player.currentPlaylist.setCurrentPosition(i);
    }

    public Track getCurrentTrack(){
        return currentPlaylist.getCurrentTrack();
    }

    public int getPlayerState() {
        return playerState;
    }

    public void setPlayerState(int playerState) {
        this.playerState = playerState;
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

    public void onDestroy(){
        if (playerState != Constants.PLAYER_STATE.PLAY){
            destroy();
        }
    }
}
