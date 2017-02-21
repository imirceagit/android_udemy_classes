package com.mient.mimusicplayer.services;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;

import com.mient.mimusicplayer.activities.MainActivity;
import com.mient.mimusicplayer.model.Track;

import java.io.IOException;

/**
 * Created by mircea.ionita on 12/6/2016.
 */

public class MediaPlayerService implements MediaPlayer.OnPreparedListener, AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnErrorListener {

    private static final String LOG_TAG = "MEDIA_PLAYER_SERVICE";

    private static MediaPlayerService instance;

    private PlayerService playerService = PlayerService.getInstance();
    private MainActivity activity = MainActivity.mainActivity;

    private MediaPlayer mMediaPlayer = null;
    private AudioManager audioManager;

    public static MediaPlayerService getInstance(){
        if(instance == null){
            instance = new MediaPlayerService();
        }
        return instance;
    }

    public MediaPlayerService(){
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
        mMediaPlayer.setOnCompletionListener(playerService);
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnErrorListener(this);
    }

    public void play(Track track){
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

    public int getCurrentProgress() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()){
            return mMediaPlayer.getCurrentPosition();
        }else {
            return 0;
        }
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
