package com.mient.radiomi.radiomi.services;

import com.mient.radiomi.radiomi.model.Playlist;
import com.mient.radiomi.radiomi.model.Station;

import java.util.ArrayList;
import java.util.List;

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

        list.add(new Station("Flight Plan (Tunes for Travel)", "flightplanmusic", "#FB5A00"));
        list.add(new Station("Two-Wheelin (Biking Playlist)", "bicyclemusic", "#50E3C2"));
        list.add(new Station("Kids Jams (Music for Children", "kidsmusic", "#FF007F"));

        return list;
    }

    public ArrayList<Station> getRecentStations(){
        ArrayList<Station> list = new ArrayList<>();

        list.add(new Station("Flight Plan (Tunes for Travel)", "flightplanmusic", "#FB5A00"));
        list.add(new Station("Two-Wheelin (Biking Playlist)", "bicyclemusic", "#50E3C2"));
        list.add(new Station("Kids Jams (Music for Children", "kidsmusic", "#FF007F"));

        return list;
    }

    public ArrayList<Station> getPartyStations(){
        ArrayList<Station> list = new ArrayList<>();

        list.add(new Station("Flight Plan (Tunes for Travel)", "flightplanmusic", "#FB5A00"));
        list.add(new Station("Two-Wheelin (Biking Playlist)", "bicyclemusic", "#50E3C2"));
        list.add(new Station("Kids Jams (Music for Children", "kidsmusic", "#FF007F"));

        return list;
    }

    public ArrayList<Playlist> getFeaturePlaylist(){

        ArrayList<Playlist> list = new ArrayList<>();

        ArrayList<String> images = new ArrayList<>();
        images.add("travelone");
        images.add("traveltwo");
        images.add("travelthree");
        images.add("travelthree");
        images.add("traveltwo");
        images.add("travelone");

        ArrayList<String> image = new ArrayList<>();
        image.add("travelone");
        image.add("traveltwo");
        image.add("travelthree");
        image.add("travelthree");
        image.add("traveltwo");
        image.add("travelone");

        list.add(new Playlist("International Playlist", "Celebrate music from around the world", images));
        list.add(new Playlist("Authentic Playlist", "Listen to music from your region", image));

        return list;
    }
}
