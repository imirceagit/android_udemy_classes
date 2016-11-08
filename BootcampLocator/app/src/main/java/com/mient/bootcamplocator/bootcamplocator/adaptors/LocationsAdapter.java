package com.mient.bootcamplocator.bootcamplocator.adaptors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mient.bootcamplocator.bootcamplocator.R;
import com.mient.bootcamplocator.bootcamplocator.holders.LocationsViewHolder;
import com.mient.bootcamplocator.bootcamplocator.model.Restaurants;
import com.mient.bootcamplocator.bootcamplocator.services.DataService;

import java.util.ArrayList;

/**
 * Created by mircea.ionita on 11/8/2016.
 */

public class LocationsAdapter extends RecyclerView.Adapter<LocationsViewHolder> {

    ArrayList<Restaurants> restaurantsList;

    public LocationsAdapter(ArrayList<Restaurants> restaurantsList) {
        this.restaurantsList = restaurantsList;
    }

    @Override
    public LocationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_location, parent, false);
        return new LocationsViewHolder(card);
    }

    @Override
    public void onBindViewHolder(LocationsViewHolder holder, int position) {
        final Restaurants restaurant = restaurantsList.get(position);
        holder.updateUI(restaurant);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Load details
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantsList.size();
    }
}
