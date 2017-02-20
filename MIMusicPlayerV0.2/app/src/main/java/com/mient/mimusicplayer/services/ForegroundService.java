package com.mient.mimusicplayer.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mient.mimusicplayer.R;
import com.mient.mimusicplayer.activities.MainActivity;
import com.mient.mimusicplayer.model.Constants;
import com.mient.mimusicplayer.model.Playlist;
import com.mient.mimusicplayer.model.Track;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mircea.ionita on 12/5/2016.
 */

public class ForegroundService extends Service {

    private static final String LOG_TAG = "FOREGROUND_SERVICE";

    private Track currentTrack;
    private int shuffleMode;
    private int repeatMode;

    private MediaPlayerService mediaPlayerService;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(LOG_TAG, "++++++++++++++++++FOREGROUND SERVICE+++++++++++++++++++++");
        mediaPlayerService = MediaPlayerService.getInstance();
        mediaPlayerService.init(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)){
            Log.v(LOG_TAG, "STARTFOREGROUND_ACTION INTENT");

            boolean onstart = false;

            Bundle extras = intent.getExtras();
            if (extras != null){
                shuffleMode = extras.getInt(Constants.KEYS.SHUFFLE);
                repeatMode = extras.getInt(Constants.KEYS.REPEAT);
                onstart = extras.getBoolean(Constants.KEYS.ONSTART);
            }

            currentTrack = Player.currentPlaylist.getCurrentTrack();

            if (!onstart){
                currentTrack = Player.currentPlaylist.getCurrentTrack();
                mediaPlayerService.play(currentTrack, Player.currentPlaylist.getCurrentPosition());
            }

        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)){
            Log.v(LOG_TAG, "PREV INTENT");

            if(shuffleMode == Constants.SHUFFLE_MODE.ON){
                currentTrack = Player.currentPlaylist.getPrevTrack(true);
                mediaPlayerService.play(currentTrack, Player.currentPlaylist.getCurrentPosition());
            }else {
                currentTrack = Player.currentPlaylist.getPrevTrack(false);
                mediaPlayerService.play(currentTrack, Player.currentPlaylist.getCurrentPosition());
            }

            Notification notification = createNotification(true);
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);

        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)){
            Log.v(LOG_TAG, "++++++++++++++++++PLAY INTENT+++++++++++++++++++++");

            currentTrack = Player.currentPlaylist.getCurrentTrack();
            mediaPlayerService.play(currentTrack, Player.currentPlaylist.getCurrentPosition());

            Notification notification = createNotification(true);
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);

        } else if (intent.getAction().equals(Constants.ACTION.RESUME_ACTION)){
            Log.v(LOG_TAG, "++++++++++++++++++RESUME INTENT+++++++++++++++++++++");

            Notification notification = createNotification(true);
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);

            mediaPlayerService.resume();

        } else if (intent.getAction().equals(Constants.ACTION.PAUSE_ACTION)){
            Log.v(LOG_TAG, "PAUSE INTENT");

            Notification notification = createNotification(false);
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);

            mediaPlayerService.pause();
            stopForeground(false);

        }else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)){
            Log.v(LOG_TAG, "NEXT INTENT");

            if(shuffleMode == Constants.SHUFFLE_MODE.ON){
                currentTrack = Player.currentPlaylist.getNextTrack(true);
                mediaPlayerService.play(currentTrack, Player.currentPlaylist.getCurrentPosition());
            }else {
                currentTrack = Player.currentPlaylist.getNextTrack(false);
                mediaPlayerService.play(currentTrack, Player.currentPlaylist.getCurrentPosition());
            }

            Notification notification = createNotification(true);
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);

        } else if (intent.getAction().equals(Constants.ACTION.STOP_ACTION)){
            Log.v(LOG_TAG, "STOP INTENT");

        } else if (intent.getAction().equals(Constants.ACTION.UPGRADE_ACTION)){
            Log.v(LOG_TAG, "UPGRADE_ACTION INTENT");
            Bundle extras = intent.getExtras();
            if (extras != null){
                shuffleMode = extras.getInt(Constants.KEYS.SHUFFLE);
                repeatMode = extras.getInt(Constants.KEYS.REPEAT);
            }

        } else if (intent.getAction().equals(Constants.ACTION.SEEK_ACTION)){
            Log.v(LOG_TAG, "SEEK_ACTION INTENT");
            int progress = 0;
            Bundle extras = intent.getExtras();
            if (extras != null){
                progress = extras.getInt(Constants.KEYS.SEEK_KEY);
            }
            int location = (int) (currentTrack.getDuration() * progress / 100);
            mediaPlayerService.seek(location);

        } else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)){
            Log.v(LOG_TAG, "STOP INTENT");
            mediaPlayerService.stop();
            mediaPlayerService.onDestroy();
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    private Notification createNotification(boolean playing){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent previousIntent = new Intent(this, ForegroundService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent pendingPrevIntent = PendingIntent.getService(this, 0, previousIntent, 0);

        Intent playIntent = new Intent(this, ForegroundService.class);
        playIntent.setAction(Constants.ACTION.RESUME_ACTION);
        PendingIntent pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0);

        Intent pauseIntent = new Intent(this, ForegroundService.class);
        pauseIntent.setAction(Constants.ACTION.PAUSE_ACTION);
        PendingIntent pendingPauseIntent = PendingIntent.getService(this, 0, pauseIntent, 0);

        Intent nextIntent = new Intent(this, ForegroundService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pendingNextIntent = PendingIntent.getService(this, 0, nextIntent, 0);

        Notification notification;

        if (playing){
            notification = new Notification.Builder(this)
                    .setContentTitle(currentTrack.getArtist())
                    .setTicker("MI Music Player")
                    .setContentText(currentTrack.getTitle())
                    .setSmallIcon(R.drawable.ic_music_video_black_48dp)
                    .setContentIntent(pendingNotificationIntent)
                    .setOngoing(true)
                    .addAction(R.drawable.ic_skip_previous_black_48dp, "", pendingPrevIntent)
                    .addAction(R.drawable.ic_pause_black_48dp, "", pendingPauseIntent)
                    .addAction(R.drawable.ic_skip_next_black_48dp, "", pendingNextIntent)
                    .build();

        }else {
            notification = new Notification.Builder(this)
                    .setContentTitle(currentTrack.getArtist())
                    .setTicker("MI Music Player TI")
                    .setContentText(currentTrack.getTitle())
                    .setSmallIcon(R.drawable.ic_music_video_black_48dp)
                    .setContentIntent(pendingNotificationIntent)
                    .addAction(R.drawable.ic_skip_previous_black_48dp, "", pendingPrevIntent)
                    .addAction(R.drawable.ic_play_arrow_black_48dp, "", pendingPlayIntent)
                    .addAction(R.drawable.ic_skip_next_black_48dp, "", pendingNextIntent)
                    .build();
        }

        return notification;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCompletition(){
        Log.v(LOG_TAG, "COMPLETED");
        if(shuffleMode == Constants.SHUFFLE_MODE.ON){
            mediaPlayerService.play(Player.currentPlaylist.getNextTrack(true), Player.currentPlaylist.getCurrentPosition());
        }else {
            if (repeatMode == Constants.REPEATE_MODE.ONE){
                mediaPlayerService.play(Player.currentPlaylist.getCurrentTrack(), Player.currentPlaylist.getCurrentPosition());
            }else if (repeatMode == Constants.REPEATE_MODE.ALL){
                mediaPlayerService.play(Player.currentPlaylist.getNextTrack(false), Player.currentPlaylist.getCurrentPosition());
            }else if (repeatMode == Constants.REPEATE_MODE.OFF){
                if (Player.currentPlaylist.isLastTrack()){
                    mediaPlayerService.stop();
                    Player.currentPlaylist.resetPlaylist();
                } else {
                    mediaPlayerService.play(Player.currentPlaylist.getNextTrack(false), Player.currentPlaylist.getCurrentPosition());
                }
            }
        }
    }
}
