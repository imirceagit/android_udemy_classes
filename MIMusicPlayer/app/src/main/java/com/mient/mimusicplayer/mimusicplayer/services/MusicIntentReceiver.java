package com.mient.mimusicplayer.mimusicplayer.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

import com.mient.mimusicplayer.mimusicplayer.MainActivity;

/**
 * Created by mircea.ionita on 11/29/2016.
 */

public class MusicIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(
                AudioManager.ACTION_HEADSET_PLUG)) {
            MainActivity.audioBecomingNoisy();
        }
    }
}
