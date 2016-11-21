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

    private ArrayList<Song> allTracksList;

    private static TracksService instance;
    private MainActivity activity;

    public static TracksService getInstance() {
        if (instance == null){
            instance = new TracksService();
        }
        return instance;
    }
    public void init(Activity activity){
        allTracksList = new ArrayList<>();
        this.activity = (MainActivity) activity;
        getMedia();
    }


    private TracksService() {
    }

    private void getMedia() {
        allTracksList.clear();
        ContentResolver contentResolver = activity.getContentResolver();
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        Log.v("TRACK-SERVICE", "GATHER MEDIA");
        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);

            do {
                allTracksList.add(new Song(cursor.getLong(idColumn), cursor.getString(titleColumn),
                        cursor.getString(artistColumn), cursor.getString(albumColumn),
                        cursor.getString(durationColumn),
                        ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getLong(idColumn)),
                                false));
                Log.v("TRACK-SERVICE", cursor.getString(titleColumn));
            } while (cursor.moveToNext());
            this.activity.setAlltracksList(allTracksList);
//            activity.updateAllTracksList();
        }
    }

    public  ArrayList<Song> getAllTracksList(){
        return getAllTracksList();
    }
}
