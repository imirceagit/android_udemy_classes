package com.mient.mimusicplayer.mimusicplayer.holders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mient.mimusicplayer.mimusicplayer.R;
import com.mient.mimusicplayer.mimusicplayer.model.Song;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * Created by mircea.ionita on 11/21/2016.
 */

public class TrackViewHolder extends RecyclerView.ViewHolder {

    private ImageView listSongAlbumArt;
    private TextView listSongTime;
    private TextView listSongArtist;
    private TextView listSongTitle;

    public TrackViewHolder(View itemView) {
        super(itemView);

        listSongAlbumArt = (ImageView) itemView.findViewById(R.id.list_song_album_art);
        listSongArtist = (TextView) itemView.findViewById(R.id.list_song_artist);
        listSongTitle = (TextView) itemView.findViewById(R.id.list_song_title);
        listSongTime = (TextView) itemView.findViewById(R.id.list_song_time);
    }

    public void updateUI(Song song){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        if(song.getCoverArtPath() == null){
            listSongAlbumArt.setImageResource(R.drawable.ic_music_video_black_48dp);
        }
        else {
            final Bitmap b = BitmapFactory.decodeFile(song.getCoverArtPath(), options);
            listSongAlbumArt.setImageBitmap(b);
        }
        listSongArtist.setText(song.getArtist());
        listSongTitle.setText(song.getTitle());
        listSongTime.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(song.getTime()),
                TimeUnit.MILLISECONDS.toSeconds(song.getTime()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes(song.getTime()))));

    }
}
