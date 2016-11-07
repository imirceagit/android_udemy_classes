package com.mient.radiomi.radiomi.services;

import com.mient.radiomi.radiomi.model.Station;

import java.util.ArrayList;

/**
 * Created by Mircea-Ionel on 11/7/2016.
 */
public class DataService {
    private static DataService ourInstance = new DataService();

    public static DataService getInstance() {
        return ourInstance;
    }

    private DataService() {

    }

    public ArrayList<Station> getFeatureStations(){

        ArrayList<Station> list = new ArrayList<>();

        list.add(new Station("Flight Plan (Tunes for Travel)", "flightplanmusic"));
        list.add(new Station("Two-Wheelin (Biking Playlist)", "bicyclemusic"));
        list.add(new Station("Kids Jams (Music for Children", "kidsmusic"));

        return list;
    }

    public ArrayList<Station> getRecentStations(){
        ArrayList<Station> list = new ArrayList<>();
        return list;
    }

    public ArrayList<Station> getPartyStations(){
        ArrayList<Station> list = new ArrayList<>();
        return list;
    }
}
