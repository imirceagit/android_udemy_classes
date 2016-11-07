package com.mient.radiomi.radiomi.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mient.radiomi.radiomi.R;
import com.mient.radiomi.radiomi.holders.PlaylistViewHolder;
import com.mient.radiomi.radiomi.model.Playlist;

import java.util.ArrayList;

/**
 * Created by mircea.ionita on 11/7/2016.
 */

public class PlaylistsAdaptor extends RecyclerView.Adapter<PlaylistViewHolder> {

    private ArrayList<Playlist> playlists;

    public PlaylistsAdaptor(ArrayList<Playlist> playlists) {
        this.playlists = playlists;
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View playlistCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_playlist, parent, false);

        return new PlaylistViewHolder(playlistCard);
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, int position) {
        final Playlist playlist = playlists.get(position);
        holder.updateUI(playlist);

        // TODO: Create onClickListener
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }
}
