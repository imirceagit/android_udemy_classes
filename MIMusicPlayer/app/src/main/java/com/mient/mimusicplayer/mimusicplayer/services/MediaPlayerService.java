package com.mient.mimusicplayer.mimusicplayer.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mient.mimusicplayer.mimusicplayer.MainActivity;
import com.mient.mimusicplayer.mimusicplayer.model.Player;
import com.mient.mimusicplayer.mimusicplayer.model.Song;

import java.io.IOException;
import java.util.Random;

/**
 * Created by mircea.ionita on 11/25/2016.
 */

public class MediaPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private static final String ACTION_PLAY = "com.mient.mimusicplayer.action.PLAY";
    MediaPlayer mMediaPlayer = null;

    private MainActivity activity = MainActivity.mainActivity;

    private Player player;

    private Song currentSong;
    private Song song;

    public void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.setDataSource(activity.getApplicationContext(), currentSong.getUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setWakeMode(activity.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.prepareAsync();
        mMediaPlayer.setOnErrorListener(this);
    }

    public void play(Song s) {
        this.song = s;
        if (mMediaPlayer == null){
            currentSong = song;
            initMediaPlayer();
        }else if (mMediaPlayer != null && !currentSong.equals(song)){
            stop();
            currentSong = song;
            initMediaPlayer();
        }else if(!mMediaPlayer.isPlaying()){
            mMediaPlayer.start();
        }
    }

    public void pause() {
        if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
    }

    public void stop(){
        if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
        clearMediaPlayer();
    }

    public void seek(int progress){
        if(mMediaPlayer != null) {
            mMediaPlayer.seekTo(progress);
        }
    }

    public int getProgress(){
        return mMediaPlayer.getCurrentPosition();
    }

    private void clearMediaPlayer(){
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    public void setPlayerHelper(Player p){
        this.player = p;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        activity.setProgressOnSeekbar();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) mMediaPlayer.release();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Random r = new Random();
        if(player.isShuffle()){
            int rand = r.nextInt(player.getPlayingList().size());
            player.setCurrentPlaying(rand);
            player.playCurrent();
        }else if (player.getRepeat() == Player.REPEAT_ONE){
            player.playCurrent();
        }else if (player.getRepeat() == Player.REPEAT_ALL && player.getCurrentPlaying() < (player.getPlayingList().size() - 1)){
            player.next();
        }else if (player.getRepeat() == Player.REPEAT_ALL && player.getCurrentPlaying() == (player.getPlayingList().size() - 1)){
            player.setCurrentPlaying(0);
            player.playCurrent();
        }else if (player.getRepeat() == Player.REPEAT_OFF && player.getCurrentPlaying() < (player.getPlayingList().size() - 1)){
            player.next();
        }else if (player.getRepeat() == Player.REPEAT_OFF && player.getCurrentPlaying() == (player.getPlayingList().size() - 1)){
            player.stop();
            activity.stopProgressBarUpdate();
        }
    }
}