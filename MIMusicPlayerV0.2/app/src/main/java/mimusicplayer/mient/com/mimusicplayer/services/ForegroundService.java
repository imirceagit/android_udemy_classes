package mimusicplayer.mient.com.mimusicplayer.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import mimusicplayer.mient.com.mimusicplayer.activities.MainActivity;
import mimusicplayer.mient.com.mimusicplayer.R;
import mimusicplayer.mient.com.mimusicplayer.model.Constants;

/**
 * Created by Mircea-Ionel on 12/2/2016.
 */

public class ForegroundService extends Service{

    private static final String LOG_TAG = "MEDIA_PLAYER_SERVICE";

    private MediaPlayer mMediaPlayer = null;

    private AudioManager audioManager;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)){
            Log.v(LOG_TAG, "START INTENT");
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            Intent previousIntent = new Intent(this, ForegroundService.class);
            previousIntent.setAction(Constants.ACTION.PREV_ACTION);
            PendingIntent pendingPreviousIntent = PendingIntent.getActivity(this, 0, previousIntent, 0);

            Intent playIntent = new Intent(this, ForegroundService.class);
            playIntent.setAction(Constants.ACTION.PLAY_ACTION);
            PendingIntent pendingPlayIntent = PendingIntent.getActivity(this, 0, playIntent, 0);

            Intent nextIntent = new Intent(this, ForegroundService.class);
            nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
            PendingIntent pendingNextIntent = PendingIntent.getActivity(this, 0, nextIntent, 0);

            Notification notification = new Notification.Builder(this)
                    .setContentTitle("MI Music Player T")
                    .setTicker("MI Music Player TI")
                    .setContentText("My Music")
                    .setSmallIcon(R.drawable.ic_menu_camera)
                    .setContentIntent(pendingNotificationIntent)
                    .setOngoing(true)
                    .addAction(R.drawable.ic_skip_previous_black_48dp, "", pendingPreviousIntent)
                    .addAction(R.drawable.ic_play_arrow_black_48dp, "", pendingPlayIntent)
                    .addAction(R.drawable.ic_skip_next_black_48dp, "", pendingNextIntent)
                    .build();

            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);


        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)){

        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)){

        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)){

        } else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)){
            Log.v(LOG_TAG, "STOP INTENT");
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
