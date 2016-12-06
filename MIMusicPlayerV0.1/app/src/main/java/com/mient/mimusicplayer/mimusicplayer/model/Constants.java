package com.mient.mimusicplayer.mimusicplayer.model;

/**
 * Created by mircea.ionita on 12/5/2016.
 */

public class Constants {

    public interface ACTION {
        String MAIN_ACTION = "com.mient.mimusicplayer.action.main";
        String PREV_ACTION = "com.mient.mimusicplayer.action.prev";
        String PLAY_ACTION = "com.mient.mimusicplayer.action.play";
        String PAUSE_ACTION = "com.mient.mimusicplayer.action.pause";
        String NEXT_ACTION = "com.mient.mimusicplayer.action.next";
        String STOP_ACTION = "com.mient.mimusicplayer.action.stop";
        String SEEK_ACTION = "com.mient.mimusicplayer.action.seek";
        String STARTFOREGROUND_ACTION = "com.mient.mimusicplayer.action.startforeground";
        String STOPFOREGROUND_ACTION = "com.mient.mimusicplayer.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }

    public interface PERMISSIONS_ID {
        int READ_EXTERNAL_STORAGE = 102;
    }

    public interface PLAYER_STATE {
        int PLAY = 1;
        int PAUSE = 2;
        int STOP = 3;
    }

    public interface REPEATE_MODE {
        int OFF = 1;
        int ALL = 2;
        int ONE = 3;
    }

    public interface SHUFFLE_MODE {
        int OFF = 0;
        int ON = 1;
    }

    public interface LAYOUT_STATE {
        int COLLAPSED = 1;
        int EXPANDED = 2;
    }

    public interface KEYS {
        String SEEK_KEY = "com.mient.mimusicplayer.key.seek";
        String PLAY_TRACK = "com.mient.mimusicplayer.key.playtrack";

        String SHUFFLE = "com.mient.mimusicplayer.key.shuffle";
        String REPEAT = "com.mient.mimusicplayer.key.repeat";
        String LAST_TRACK = "com.mient.mimusicplayer.key.lasttrack";
    }
}
