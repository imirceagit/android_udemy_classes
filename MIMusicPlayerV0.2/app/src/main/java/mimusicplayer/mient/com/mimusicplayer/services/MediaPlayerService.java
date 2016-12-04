package mimusicplayer.mient.com.mimusicplayer.services;

import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by Mircea-Ionel on 12/4/2016.
 */

public class MediaPlayerService implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, AudioManager.OnAudioFocusChangeListener{


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
