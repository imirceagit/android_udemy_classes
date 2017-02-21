package com.mient.mimusicplayer.services;

import android.media.MediaPlayer;
import android.util.Log;

import com.mient.mimusicplayer.activities.MainActivity;
import com.mient.mimusicplayer.model.Constants;
import com.mient.mimusicplayer.model.Playlist;
import com.mient.mimusicplayer.model.Track;

import java.util.List;
import java.util.Random;

/**
 * Created by mircea.ionita on 2/21/2017.
 */

public class PlayerService implements MediaPlayer.OnCompletionListener{

    private static final String LOG_TAG = "PLAYER_SERVICE";

    private static PlayerService instance;

    private MediaPlayerService mediaPlayerService;
    private MainActivity activity = MainActivity.mainActivity;

    private int playerState = Constants.PLAYER_STATE.STOP;
    private Playlist activePlaylist;
    private List<Track> trackList;
    private int position;
    private int shuffleMode;
    private int repeatMode;

    private Random rand = new Random();

    public static PlayerService getInstance(){
        if (instance == null){
            instance = new PlayerService();
        }

        return instance;
    }

    public void init(Playlist activePlaylist, int position, int shuffleMode, int repeatMode) {
        this.activePlaylist = activePlaylist;
        this.trackList = this.activePlaylist.getTrackList();
        this.position = position;
        this.shuffleMode = shuffleMode;
        this.repeatMode = repeatMode;
        mediaPlayerService = MediaPlayerService.getInstance();
    }

    public Track getCurrentTrack(){
        return trackList.get(position);
    }

    public Track getNextTrack(){
        if (getShuffleMode() == Constants.SHUFFLE_MODE.ON){
            position = rand.nextInt(activePlaylist.getTrackCount());
        }else {
            if (isLastTrack()){
                position = 0;
            } else {
                ++position;
            }
        }

        activity.updatePlayerUI();
        return trackList.get(position);
    }

    public Track getPrevTrack(){
        if (getShuffleMode() == Constants.SHUFFLE_MODE.ON){
            position = rand.nextInt(activePlaylist.getTrackCount());
        }else {
            if (isFirstTrack()){
                position = (activePlaylist.getTrackCount() - 1);
            } else {
                --position;
            }
        }

        activity.updatePlayerUI();
        return trackList.get(position);
    }

    public void updatePreferences(int shuffle, int repeat){
        setShuffleMode(shuffle);
        setRepeatMode(repeat);
    }

    public void play(){
        setPlayerState(Constants.PLAYER_STATE.PLAY);
        activity.updatePlayerUI();
    }

    public void pause(){
        setPlayerState(Constants.PLAYER_STATE.PAUSE);
        activity.updatePlayerUI();
    }

    public int getPlayerState() {
        return playerState;
    }

    public void setPlayerState(int playerState) {
        this.playerState = playerState;
    }

    public int getProgress(){
        return mediaPlayerService.getCurrentProgress();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (repeatMode == Constants.REPEATE_MODE.ONE){
            mediaPlayerService.play(getCurrentTrack());
            Log.v(LOG_TAG, " ==ON COMPLETION ============= ONE");
        }else {
            mediaPlayerService.play(getNextTrack());
            Log.v(LOG_TAG, " ==ON COMPLETION ============= NEXT");
        }
    }

    public boolean isLastTrack(){
        if (position == activePlaylist.getTrackCount() - 1){
            return true;
        }else {
            return false;
        }
    }

    public boolean isFirstTrack(){
        if (position == 0){
            return true;
        }else {
            return false;
        }
    }

    public Playlist getActivePlaylist() {
        return activePlaylist;
    }

    public int getPosition() {
        return position;
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
