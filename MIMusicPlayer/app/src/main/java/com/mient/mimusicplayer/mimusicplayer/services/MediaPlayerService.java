package com.mient.mimusicplayer.mimusicplayer.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mient.mimusicplayer.mimusicplayer.MainActivity;
import com.mient.mimusicplayer.mimusicplayer.R;
import com.mient.mimusicplayer.mimusicplayer.model.Player;
import com.mient.mimusicplayer.mimusicplayer.model.Song;

import java.io.IOException;
import java.util.Random;

/**
 * Created by mircea.ionita on 11/25/2016.
 */

public class MediaPlayerService implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {

    private static final String LOG_TAG = "MEDIA_PLAYER_SERVICE";

    private static final int NOTIFICATION_ID = 1234;

    private static final String ACTION_PLAY = "com.mient.mimusicplayer.action.PLAY";
    MediaPlayer mMediaPlayer = null;

    AudioManager audioManager;

    private MainActivity activity = MainActivity.mainActivity;

    private Player player;

    private Song currentSong;
    private Song song;

    private Notification notification;

    public MediaPlayerService(Player player) {
        this.player = player;
    }

    public void onCreate(){

        Intent notificationIntent = new Intent(activity, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, notificationIntent, 0);

        notification = new Notification.Builder(activity)
                .setContentTitle("MI Music Player")
                .setContentText("Music")
                .setSmallIcon(R.drawable.ic_music_video_black_48dp)
                .build();

//        startForeground(NOTIFICATION_ID, notification);

        audioManager = (AudioManager) activity.getSystemService(activity.AUDIO_SERVICE);
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
    }

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
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) mMediaPlayer.pause();
    }

    public void stop(){
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) mMediaPlayer.stop();
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
//        stopForeground(true);
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

    public void onDestroy() {
        if (mMediaPlayer != null) mMediaPlayer.release();
        mMediaPlayer = null;
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

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                Log.v("FOCUS", "AUDIOFOCUS_GAIN");
                if (mMediaPlayer == null) initMediaPlayer();
                else if (!mMediaPlayer.isPlaying()) mMediaPlayer.start();
                mMediaPlayer.setVolume(1.0f, 1.0f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                Log.v("FOCUS", "AUDIOFOCUS_LOSS");
                if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                Log.v("FOCUS", "AUDIOFOCUS_LOSS_TRANSIENT");
                if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                Log.v("FOCUS", "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                if (mMediaPlayer.isPlaying()) mMediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }
}
