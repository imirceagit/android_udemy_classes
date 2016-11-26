package com.mient.mimusicplayer.mimusicplayer.services;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.mient.mimusicplayer.mimusicplayer.MainActivity;
import com.mient.mimusicplayer.mimusicplayer.model.Song;

import java.util.ArrayList;

/**
 * Created by mircea.ionita on 11/21/2016.
 */
public class TracksService {

    private static TracksService instance;

    private TracksService() {
    }

    public static TracksService getInstance() {
        if (instance == null){
            instance = new TracksService();
        }
        return instance;
    }
    public void init(MainActivity activity){
        getMedia();
    }

    private void getMedia() {
        MainActivity.allTracksList.clear();
        ContentResolver contentResolver = MainActivity.mainActivity.getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, MediaStore.Audio.Media.IS_MUSIC + " = 1", null, null);
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            MainActivity.displayToast("No media found.");
        } else {
            int idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
//            int audioIdColumn = cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumIdColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);

            do {
//                long albumId = Long.getLong(cursor.getString(albumIdColumn));
                MainActivity.allTracksList.add(new Song(cursor.getLong(idColumn), cursor.getLong(idColumn), cursor.getString(titleColumn),
                        cursor.getString(artistColumn), 1, cursor.getString(albumColumn),
                        cursor.getLong(durationColumn), ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getLong(idColumn)),
                                false));
            } while (cursor.moveToNext());
        }
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
        Log.v("ALBUM", result);
        return result;
    }
}
