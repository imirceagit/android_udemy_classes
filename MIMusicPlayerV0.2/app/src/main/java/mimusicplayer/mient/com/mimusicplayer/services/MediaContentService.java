package mimusicplayer.mient.com.mimusicplayer.services;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import mimusicplayer.mient.com.mimusicplayer.activities.MainActivity;
import mimusicplayer.mient.com.mimusicplayer.model.Track;

/**
 * Created by Mircea-Ionel on 12/3/2016.
 */

public class MediaContentService {

    private static MediaContentService mMediaContentService;

    public MediaContentService() {
    }

    public static MediaContentService getInstance(){
        if (mMediaContentService == null){
            mMediaContentService = new MediaContentService();
        }
        return mMediaContentService;
    }

    private AsyncQueryHandler queryHandler = new AsyncQueryHandler(MainActivity.mainActivity.getContentResolver()) {
        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (cursor == null) {
                // query failed, handle error.
            } else if (!cursor.moveToFirst()) {
                MainActivity.displayToast("No media found.");
            } else {
                int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                int audioIdColumn = cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID);
                int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
                int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);

                do {
                    long albumId = Long.parseLong((cursor.getString(albumIdColumn) == null ? "0" : cursor.getString(albumIdColumn)));
                    MainActivity.allTracksList.add(new Track(cursor.getLong(idColumn), cursor.getLong(idColumn), cursor.getString(titleColumn),
                            cursor.getString(artistColumn), albumId, cursor.getString(albumColumn), cursor.getLong(durationColumn),
                            ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getLong(idColumn)), getCoverArtPath(albumId)));
                } while (cursor.moveToNext());
            }
        }
    };

    public void queryAllTracks(){
        MainActivity.allTracksList.clear();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        queryHandler.startQuery(1, null, uri, null, MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);
    }

    private static String getCoverArtPath(long albumId) {
        Cursor albumCursor = MainActivity.mainActivity.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + " = ?",
                new String[]{Long.toString(albumId)},
                null
        );
        boolean queryResult = albumCursor.moveToFirst();
        String result = null;
        if (queryResult) {
            result = albumCursor.getString(0);
        }
        albumCursor.close();
        return result;
    }

}
