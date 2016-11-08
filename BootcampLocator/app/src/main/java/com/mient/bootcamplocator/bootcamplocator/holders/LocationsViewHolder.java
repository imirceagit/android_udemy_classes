package com.mient.bootcamplocator.bootcamplocator.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mient.bootcamplocator.bootcamplocator.R;
import com.mient.bootcamplocator.bootcamplocator.model.Restaurants;

/**
 * Created by mircea.ionita on 11/8/2016.
 */

public class LocationsViewHolder extends RecyclerView.ViewHolder {

    private TextView cardTitle;
    private TextView cardAddress;
    private ImageView cardImg;

    public LocationsViewHolder(View itemView) {
        super(itemView);

        cardTitle = (TextView) itemView.findViewById(R.id.card_title);
        cardAddress = (TextView) itemView.findViewById(R.id.card_address);
        cardImg = (ImageView) itemView.findViewById(R.id.card_image);
    }

    public void updateUI(Restaurants item){
        cardTitle.setText(item.getLocationTitle());
        cardAddress.setText(item.getLocationAddress());
        int resourceId = cardImg.getResources().getIdentifier(item.getImgUri(), null, cardImg.getContext().getPackageName());
        cardImg.setImageResource(resourceId);
    }
}
