package com.mient.mimusicplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mient.mimusicplayer.R;
import com.mient.mimusicplayer.activities.MainActivity;
import com.mient.mimusicplayer.holders.TrackViewHolder;
import com.mient.mimusicplayer.model.Playlist;
import com.mient.mimusicplayer.model.Track;

import java.util.ArrayList;

/**
 * Created by mircea.ionita on 11/21/2016.
 */

public class TracksAdapter extends RecyclerView.Adapter<TrackViewHolder> {

    private MainActivity activity = MainActivity.mainActivity;

    private Playlist list;

    public TracksAdapter(Playlist playlist) {
        list = playlist;
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_track, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrackViewHolder holder, int position) {
        final Track song = list.getTrackList().get(position);
        final int currentPosition = position;
        holder.updateUI(song);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setCurrentTrackPlaying(currentPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MainActivity.defaultPlaylist.getTrackCount();
    }
}
