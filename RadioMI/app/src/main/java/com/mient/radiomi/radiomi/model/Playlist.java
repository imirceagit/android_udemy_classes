package com.mient.radiomi.radiomi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mircea.ionita on 11/7/2016.
 */

public class Playlist {

    final String DRAWABLE = "drawable/";

    private String playlistName;
    private String playlistDetails;
    private ArrayList<String> imagUri;

    public Playlist(String playlistName, String playlistDetails, ArrayList<String> imagUri) {
        this.playlistName = playlistName;
        this.playlistDetails = playlistDetails;
        this.imagUri = imagUri;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public String getPlaylistDetails() {
        return playlistDetails;
    }

    public List<String> getImagUri() {
        for(int i = 0; i < imagUri.size(); i++){
            String current = imagUri.get(i);
            imagUri.set(i, DRAWABLE + current);
        }
        return imagUri;
    }
}
