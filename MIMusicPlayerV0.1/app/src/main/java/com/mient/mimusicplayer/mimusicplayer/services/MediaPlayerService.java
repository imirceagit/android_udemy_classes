package com.mient.mimusicplayer.mimusicplayer.services;

import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by mircea.ionita on 12/5/2016.
 */

public class MediaPlayerService implements AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }
}
