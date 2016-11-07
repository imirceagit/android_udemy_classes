package com.mient.radiomi.radiomi.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mient.radiomi.radiomi.R;
import com.mient.radiomi.radiomi.model.Playlist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mircea.ionita on 11/7/2016.
 */

public class PlaylistViewHolder extends RecyclerView.ViewHolder{

    private ImageView imageOne;
    private ImageView imageTwo;
    private ImageView imageThree;
    private ImageView imageFour;
    private ImageView imageFive;
    private ImageView imageSix;
    private TextView playlistName;
    private TextView playlistDetails;

    public PlaylistViewHolder(View itemView) {
        super(itemView);

        this.imageOne = (ImageView) itemView.findViewById(R.id.image_one);
        this.imageTwo = (ImageView) itemView.findViewById(R.id.image_two);
        this.imageThree = (ImageView) itemView.findViewById(R.id.image_three);
        this.imageFour = (ImageView) itemView.findViewById(R.id.image_four);
        this.imageFive = (ImageView) itemView.findViewById(R.id.image_five);
        this.imageSix = (ImageView) itemView.findViewById(R.id.image_six);
        this.playlistName = (TextView) itemView.findViewById(R.id.playlist_name);
        this.playlistDetails = (TextView) itemView.findViewById(R.id.playlist_details);
    }

    public void updateUI(Playlist playlist){
        ArrayList<String> uri = (ArrayList<String>) playlist.getImagUri();
        int resource = imageOne.getResources().getIdentifier(uri.get(0), null, imageOne.getContext().getPackageName());
        imageOne.setImageResource(resource);
        resource = imageTwo.getResources().getIdentifier(uri.get(1), null, imageOne.getContext().getPackageName());
        imageTwo.setImageResource(resource);
        resource = imageThree.getResources().getIdentifier(uri.get(2), null, imageOne.getContext().getPackageName());
        imageThree.setImageResource(resource);
        resource = imageFour.getResources().getIdentifier(uri.get(3), null, imageOne.getContext().getPackageName());
        imageFour.setImageResource(resource);
        resource = imageFive.getResources().getIdentifier(uri.get(4), null, imageOne.getContext().getPackageName());
        imageFive.setImageResource(resource);
        resource = imageSix.getResources().getIdentifier(uri.get(5), null, imageOne.getContext().getPackageName());
        imageSix.setImageResource(resource);

        playlistName.setText(playlist.getPlaylistName());
        playlistDetails.setText(playlist.getPlaylistDetails());
    }
}
