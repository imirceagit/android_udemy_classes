package com.mient.mimusicplayer.mimusicplayer.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mient.mimusicplayer.mimusicplayer.model.Constants;
import com.mient.mimusicplayer.mimusicplayer.activities.MainActivity;
import com.mient.mimusicplayer.mimusicplayer.R;
import com.mient.mimusicplayer.mimusicplayer.model.Player;
import com.mient.mimusicplayer.mimusicplayer.model.Track;

import java.util.ArrayList;

/**
 * Created by mircea.ionita on 12/5/2016.
 */

public class ForegroundService extends Service {

    private static final String LOG_TAG = "MEDIA_PLAYER_SERVICE";

    private MediaPlayerService mediaPlayerService;
    private int currentTrack;

    private ArrayList<Track> tracksList = new ArrayList<>();

    @Override
    public void onCreate() {
        tracksList = MainActivity.allTracksList;
        mediaPlayerService = MediaPlayerService.getInstance();
        mediaPlayerService.init(tracksList, currentTrack);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)){
            Log.v(LOG_TAG, "PREV INTENT");
            mediaPlayerService.prev();

        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)){
            Log.v(LOG_TAG, "PLAY INTENT");

            Bundle extras = intent.getExtras();
            if (extras != null){
                currentTrack = extras.getInt(Constants.KEYS.PLAY_TRACK);
            }

            mediaPlayerService.play(tracksList.get(currentTrack));

            Notification notification = createNotification(true);
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);

        } else if (intent.getAction().equals(Constants.ACTION.PAUSE_ACTION)){
            Log.v(LOG_TAG, "PAUSE INTENT");

            mediaPlayerService.pause();
            Notification notification = createNotification(false);
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);

        }else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)){
            Log.v(LOG_TAG, "NEXT INTENT");

            mediaPlayerService.next();
        } else if (intent.getAction().equals(Constants.ACTION.STOP_ACTION)){
            Log.v(LOG_TAG, "STOP INTENT");
            mediaPlayerService.stop();

        } else if (intent.getAction().equals(Constants.ACTION.SEEK_ACTION)){
            Log.v(LOG_TAG, "SEEK INTENT");
            int progress;
            Bundle extras = intent.getExtras();
            if (extras != null){
                progress = extras.getInt(Constants.KEYS.SEEK_KEY);
                mediaPlayerService.seek(progress);
            }
        } else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)){
            Log.v(LOG_TAG, "STOP INTENT");
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
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
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
                    .setContentTitle("MI Music Player T")
                    .setTicker("MI Music Player TI")
                    .setContentText("My Music")
                    .setSmallIcon(R.drawable.ic_menu_camera)
                    .setContentIntent(pendingNotificationIntent)
                    .setOngoing(true)
                    .addAction(R.drawable.ic_skip_previous_black_48dp, "PREV", pendingPrevIntent)
                    .addAction(R.drawable.ic_pause_black_48dp, "PAUSE", pendingPauseIntent)
                    .addAction(R.drawable.ic_skip_next_black_48dp, "NEXT", pendingNextIntent)
                    .build();

        }else {
            notification = new Notification.Builder(this)
                    .setContentTitle("MI Music Player T")
                    .setTicker("MI Music Player TI")
                    .setContentText("My Music")
                    .setSmallIcon(R.drawable.ic_menu_camera)
                    .setContentIntent(pendingNotificationIntent)
                    .setOngoing(false)
                    .addAction(R.drawable.ic_skip_previous_black_48dp, "PREV", pendingPrevIntent)
                    .addAction(R.drawable.ic_play_arrow_black_48dp, "PLAY", pendingPlayIntent)
                    .addAction(R.drawable.ic_skip_next_black_48dp, "NEXT", pendingNextIntent)
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
}
