package com.mient.bootcamplocator.bootcamplocator.fragments;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mient.bootcamplocator.bootcamplocator.R;
import com.mient.bootcamplocator.bootcamplocator.model.Restaurants;
import com.mient.bootcamplocator.bootcamplocator.services.DataService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;
    private MarkerOptions userMarker;
    private LocationsListFragment fragment;

    int zip;

    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fragment = (LocationsListFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.container_locations_list);

        if(fragment == null){
            fragment = LocationsListFragment.newInstance();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container_locations_list, fragment)
                    .commit();
        }

        final EditText searchText = (EditText) v.findViewById(R.id.search_text);

        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER){
                    String text = searchText.getText().toString();
                    int zip = Integer.parseInt(text);

                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(searchText.getWindowToken(), 0);

                    fragment.updateDataset(zip);
                    showList();
                    updateMapForZip(zip);
                    return true;
                }
                return false;
            }
        });

        hideList();
        return v;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                hideList();
            }
        });

        LatLng position = new LatLng(44.433298, 26.110805);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
    }

    public void setUserMarker(LatLng latLong){
        if(userMarker == null){
            userMarker = new MarkerOptions().position(latLong).title("Current location");
            mMap.addMarker(userMarker);
        }

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(latLong.latitude, latLong.longitude, 1);
            zip = Integer.parseInt(addressList.get(0).getPostalCode());
            updateMapForZip(zip);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLong, 15));
    }

    private void updateMapForZip(int zipcode){
        ArrayList<Restaurants> locations = DataService.getInstance().getCoursesLocationsWithin10MilesOfZip(zipcode);

        for(int i = 0; i < locations.size(); i++){
            Restaurants loc = locations.get(i);
            MarkerOptions marker = new MarkerOptions().position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                    .title(loc.getLocationTitle())
                    .snippet(loc.getLocationAddress())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
            mMap.addMarker(marker);
        }
    }

    private void hideList(){
        getActivity().getSupportFragmentManager().beginTransaction().hide(fragment).commit();
    }

    private void showList(){
        getActivity().getSupportFragmentManager().beginTransaction().show(fragment).commit();
    }
}
