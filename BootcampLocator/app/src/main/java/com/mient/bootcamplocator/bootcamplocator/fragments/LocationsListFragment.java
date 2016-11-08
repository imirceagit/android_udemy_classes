package com.mient.bootcamplocator.bootcamplocator.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mient.bootcamplocator.bootcamplocator.R;
import com.mient.bootcamplocator.bootcamplocator.adaptors.LocationsAdapter;
import com.mient.bootcamplocator.bootcamplocator.model.Restaurants;
import com.mient.bootcamplocator.bootcamplocator.services.DataService;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocationsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationsListFragment extends Fragment {

    private ArrayList<Restaurants> restaurantsList;
    LocationsAdapter adapter;

    public LocationsListFragment() {
        // Required empty public constructor
    }

    public static LocationsListFragment newInstance() {
        LocationsListFragment fragment = new LocationsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locations_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_locations);
        recyclerView.setHasFixedSize(true);

        restaurantsList = DataService.getInstance().getCoursesLocationsWithin10MilesOfZip(123);
        adapter = new LocationsAdapter(restaurantsList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    public void updateDataset(int zipcode){
        restaurantsList = DataService.getInstance().getCoursesLocationsWithin10MilesOfZip(zipcode);
        adapter.notifyDataSetChanged();
    }

}
