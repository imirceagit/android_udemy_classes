package com.mient.mimusicplayer.mimusicplayer.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mient.mimusicplayer.mimusicplayer.R;
import com.mient.mimusicplayer.mimusicplayer.model.Song;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

/**
 * Created by mircea.ionita on 11/21/2016.
 */

public class TrackViewHolder extends RecyclerView.ViewHolder {

    private TextView listSongTime;
    private TextView listSongArtist;
    private TextView listSongTitle;

    public TrackViewHolder(View itemView) {
        super(itemView);

        listSongArtist = (TextView) itemView.findViewById(R.id.list_song_artist);
        listSongTitle = (TextView) itemView.findViewById(R.id.list_song_title);
        listSongTime = (TextView) itemView.findViewById(R.id.list_song_time);
    }

    public void updateUI(Song song){
        listSongArtist.setText(song.getArtist());
        listSongTitle.setText(song.getTitle());
        listSongTime.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(song.getTime()),
                TimeUnit.MILLISECONDS.toSeconds(song.getTime()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes(song.getTime()))));

    }
}
