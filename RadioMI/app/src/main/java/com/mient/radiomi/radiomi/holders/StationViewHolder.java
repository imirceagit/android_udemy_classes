package com.mient.radiomi.radiomi.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mient.radiomi.radiomi.R;
import com.mient.radiomi.radiomi.model.Station;

/**
 * Created by Mircea-Ionel on 11/7/2016.
 */

public class StationViewHolder extends RecyclerView.ViewHolder{

    private ImageView mainImage;
    private TextView mainTitle;

    public StationViewHolder(View itemView) {
        super(itemView);

        this.mainImage = (ImageView) itemView.findViewById(R.id.main_img);
        this.mainTitle = (TextView) itemView.findViewById(R.id.main_title);
    }

    public void updateUI(Station station){
        String uri = station.getImgUri();
        int resource = mainImage.getResources().getIdentifier(uri, null, mainImage.getContext().getPackageName());
        mainImage.setImageResource(resource);

        mainTitle.setText(station.getStationTitle());
    }
}
