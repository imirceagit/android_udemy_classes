package com.mient.mimusicplayer.mimusicplayer.services;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;

import com.mient.mimusicplayer.mimusicplayer.activities.MainActivity;
import com.mient.mimusicplayer.mimusicplayer.model.Constants;
import com.mient.mimusicplayer.mimusicplayer.model.Player;
import com.mient.mimusicplayer.mimusicplayer.model.Track;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mircea.ionita on 12/6/2016.
 */

public class MediaPlayerService implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnErrorListener {

    private MainActivity activity = MainActivity.mainActivity;

    private ArrayList<Track> tracksList = new ArrayList<>();
    private int currentTrackPosition;

    private Player player = MainActivity.mPlayer;

    private MediaPlayer mMediaPlayer = null;
    private AudioManager audioManager;

    private Track currentTrack;

    private static MediaPlayerService instance;

    public static MediaPlayerService getInstance(){
        if(instance == null){
            instance = new MediaPlayerService();
        }
        return instance;
    }

    public MediaPlayerService() {

    }

    public void init(ArrayList<Track> tracksList, int currentTrackPosition){
        this.tracksList = tracksList;
        this.currentTrackPosition = currentTrackPosition;
    }


    private void prepareMediaPlayer(){
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(activity.getApplicationContext(), currentTrack.getUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setWakeMode(activity.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnErrorListener(this);
    }

    public void play(Track track){
        if (mMediaPlayer == null){
            currentTrack = track;
            prepareMediaPlayer();
        }else if (mMediaPlayer != null && !currentTrack.equals(track)){
            stop();
            currentTrack = track;
            prepareMediaPlayer();
        }else if(!mMediaPlayer.isPlaying()){
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

    public void prev(){
        Random r = new Random();
        if(player.getShuffleMode() == Constants.SHUFFLE_MODE.ON){
            int rand = r.nextInt(tracksList.size());
            player.setCurrentPlayingPosition(rand);
        }else if(player.getCurrentPlayingPosition() > 0 ){
            int pos = player.getCurrentPlayingPosition();
            player.setCurrentPlayingPosition(--pos);
        }else if (player.getCurrentPlayingPosition() == 0){
            player.setCurrentPlayingPosition(tracksList.size() - 1);
        }
        play(tracksList.get(player.getCurrentPlayingPosition()));
        activity.updatePlayerUI();

    }

    public void next(){
        Random r = new Random();
        if(player.getShuffleMode() == Constants.SHUFFLE_MODE.ON){
            int rand = r.nextInt(tracksList.size());
            player.setCurrentPlayingPosition(rand);
        }else if(player.getCurrentPlayingPosition() < tracksList.size() - 1){
            int pos = player.getCurrentPlayingPosition();
            player.setCurrentPlayingPosition(++pos);
        }else if (player.getCurrentPlayingPosition() == tracksList.size() - 1){
            player.setCurrentPlayingPosition(0);
        }
        play(tracksList.get(player.getCurrentPlayingPosition()));
        activity.updatePlayerUI();
    }

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
        Random r = new Random();
        if(player.getShuffleMode() == Constants.SHUFFLE_MODE.ON){
            int rand = r.nextInt(tracksList.size());
            player.setCurrentPlayingPosition(rand);
            play(tracksList.get(player.getCurrentPlayingPosition()));
        }else if (player.getRepeatMode() == Constants.REPEATE_MODE.ONE){
            play(tracksList.get(player.getCurrentPlayingPosition()));
        }else if (player.getRepeatMode() == Constants.REPEATE_MODE.ALL && player.getCurrentPlayingPosition() < (tracksList.size() - 1)){
            player.next();
        }else if (player.getRepeatMode() == Constants.REPEATE_MODE.ALL && player.getCurrentPlayingPosition() == (tracksList.size() - 1)){
            player.setCurrentPlayingPosition(0);
            play(tracksList.get(player.getCurrentPlayingPosition()));
        }else if (player.getRepeatMode() == Constants.REPEATE_MODE.OFF && player.getCurrentPlayingPosition() < (tracksList.size() - 1)){
            player.next();
        }else if (player.getRepeatMode() == Constants.REPEATE_MODE.OFF && player.getCurrentPlayingPosition() == (tracksList.size() - 1)){
            player.stop();
        }
        activity.updatePlayerUI();
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
