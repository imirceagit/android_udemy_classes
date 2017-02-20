package com.mient.mimusicplayer.services;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;

import com.mient.mimusicplayer.activities.MainActivity;
import com.mient.mimusicplayer.model.Constants;
import com.mient.mimusicplayer.model.Playlist;
import com.mient.mimusicplayer.model.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mircea.ionita on 12/6/2016.
 */

public class MediaPlayerService implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnErrorListener {

    private static final String LOG_TAG = "MEDIA_PLAYER_SERVICE";

    private MainActivity activity = MainActivity.mainActivity;
    private ForegroundService foregroundService;

    private MediaPlayer mMediaPlayer = null;
    private AudioManager audioManager;

    private Track currentPlaying;
    private int pos;

    private static MediaPlayerService instance;

    public static MediaPlayerService getInstance(){
        if(instance == null){
            instance = new MediaPlayerService();
        }
        return instance;
    }

    public MediaPlayerService(){
    }

    public void init(ForegroundService fs) {
        foregroundService = fs;
    }

    private void prepareMediaPlayer(Track track){
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(activity.getApplicationContext(), track.getUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setWakeMode(activity.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnErrorListener(this);
    }

    public void play(Track track, int position){
        pos = position;
        if (mMediaPlayer == null){
            prepareMediaPlayer(track);
        }else if (mMediaPlayer != null){
            stop();
            prepareMediaPlayer(track);
        }else if(!mMediaPlayer.isPlaying()){
            mMediaPlayer.start();
        }
    }

    public void resume(){
        if(!mMediaPlayer.isPlaying()){
            mMediaPlayer.start();
        }
    }

    public void pause(){
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) mMediaPlayer.pause();
    }

    public void stop(){
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) mMediaPlayer.stop();
        clearMediaPlayer();
    }

//    public void prev(){
//        Random r = new Random();
//        if(player.getShuffleMode() == Constants.SHUFFLE_MODE.ON){
//            int rand = r.nextInt(tracksList.size());
//            player.setCurrentPlayingPosition(rand);
//        }else if(player.getCurrentPlayingPosition() > 0 ){
//            int pos = player.getCurrentPlayingPosition();
//            player.setCurrentPlayingPosition(--pos);
//        }else if (player.getCurrentPlayingPosition() == 0){
//            player.setCurrentPlayingPosition(tracksList.size() - 1);
//        }
//        play(tracksList.get(player.getCurrentPlayingPosition()));
//        activity.updatePlayerUI();
//
//    }

//    public void next(){
//        Random r = new Random();
//        if(player.getShuffleMode() == Constants.SHUFFLE_MODE.ON){
//            int rand = r.nextInt(tracksList.size());
//            player.setCurrentPlayingPosition(rand);
//        }else if(player.getCurrentPlayingPosition() < tracksList.size() - 1){
//            int pos = player.getCurrentPlayingPosition();
//            player.setCurrentPlayingPosition(++pos);
//        }else if (player.getCurrentPlayingPosition() == tracksList.size() - 1){
//            player.setCurrentPlayingPosition(0);
//        }
//        play(tracksList.get(player.getCurrentPlayingPosition()));
//        activity.updatePlayerUI();
//    }

    public void seek(int progress){
        mMediaPlayer.seekTo(progress);
    }

    private void clearMediaPlayer(){
        if (mMediaPlayer != null) mMediaPlayer.release();
        mMediaPlayer = null;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
//        Random r = new Random();
//        if(player.getShuffleMode() == Constants.SHUFFLE_MODE.ON){
//            int rand = r.nextInt(tracksList.size());
//            player.setCurrentPlayingPosition(rand);
//            play(tracksList.get(player.getCurrentPlayingPosition()));
//        }else if (player.getRepeatMode() == Constants.REPEATE_MODE.ONE){
//            play(tracksList.get(player.getCurrentPlayingPosition()));
//        }else if (player.getRepeatMode() == Constants.REPEATE_MODE.ALL && player.getCurrentPlayingPosition() < (tracksList.size() - 1)){
//            player.next();
//        }else if (player.getRepeatMode() == Constants.REPEATE_MODE.ALL && player.getCurrentPlayingPosition() == (tracksList.size() - 1)){
//            player.setCurrentPlayingPosition(0);
//            play(tracksList.get(player.getCurrentPlayingPosition()));
//        }else if (player.getRepeatMode() == Constants.REPEATE_MODE.OFF && player.getCurrentPlayingPosition() < (tracksList.size() - 1)){
//            player.next();
//        }else if (player.getRepeatMode() == Constants.REPEATE_MODE.OFF && player.getCurrentPlayingPosition() == (tracksList.size() - 1)){
//            player.stop();
//        }
//        activity.updatePlayerUI();
        foregroundService.onCompletition();
    }

    public int getCurrentProgress() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()){
            return mMediaPlayer.getCurrentPosition();
        }else {
            return 0;
        }
    }

    public int getCurrentPlaying(){
        return pos;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void onDestroy() {
        clearMediaPlayer();
    }
}
