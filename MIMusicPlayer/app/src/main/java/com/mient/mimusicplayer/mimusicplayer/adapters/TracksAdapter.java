package com.mient.mimusicplayer.mimusicplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mient.mimusicplayer.mimusicplayer.MainActivity;
import com.mient.mimusicplayer.mimusicplayer.R;
import com.mient.mimusicplayer.mimusicplayer.holders.TrackViewHolder;
import com.mient.mimusicplayer.mimusicplayer.model.Song;

import java.util.ArrayList;

/**
 * Created by mircea.ionita on 11/21/2016.
 */

public class TracksAdapter extends RecyclerView.Adapter<TrackViewHolder> {

    private MainActivity activity = MainActivity.mainActivity;

    private static ArrayList<Song> songList = new ArrayList<Song>();

    public TracksAdapter(ArrayList<Song> songList) {
        this.songList = songList;
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_track, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackViewHolder holder, int position) {
        final Song song = songList.get(position);
        final int currentPosition = position;
        holder.updateUI(song);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setCurrentTrack(songList, currentPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
}
