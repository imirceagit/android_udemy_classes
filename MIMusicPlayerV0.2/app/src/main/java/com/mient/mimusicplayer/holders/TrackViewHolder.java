package com.mient.mimusicplayer.holders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mient.mimusicplayer.R;
import com.mient.mimusicplayer.model.Track;

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

    public void updateUI(Track track){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        if(track.getCoverArtPath() == null){
            listSongAlbumArt.setImageResource(R.drawable.ic_music_video_black_48dp);
        }
        else {
            final Bitmap b = BitmapFactory.decodeFile(track.getCoverArtPath(), options);
            listSongAlbumArt.setImageBitmap(b);
        }
        listSongArtist.setText(track.getArtist());
        listSongTitle.setText(track.getTitle());
        listSongTime.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(track.getDuration()),
                TimeUnit.MILLISECONDS.toSeconds(track.getDuration()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes(track.getDuration()))));

    }
}
