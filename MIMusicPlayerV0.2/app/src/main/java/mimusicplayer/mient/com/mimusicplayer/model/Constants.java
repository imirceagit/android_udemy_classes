package mimusicplayer.mient.com.mimusicplayer.model;

/**
 * Created by Mircea-Ionel on 12/2/2016.
 */

public class Constants {

    public interface ACTION {
        String MAIN_ACTION = "com.mient.mimusicplayer.action.main";
        String PREV_ACTION = "com.mient.mimusicplayer.action.prev";
        String PLAY_ACTION = "com.mient.mimusicplayer.action.play";
        String PAUSE_ACTION = "com.mient.mimusicplayer.action.pause";
        String NEXT_ACTION = "com.mient.mimusicplayer.action.next";
        String STARTFOREGROUND_ACTION = "com.mient.mimusicplayer.action.startforeground";
        String STOPFOREGROUND_ACTION = "com.mient.mimusicplayer.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }
}
