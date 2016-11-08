package com.mient.bootcamplocator.bootcamplocator.services;

import com.mient.bootcamplocator.bootcamplocator.model.Restaurants;

import java.util.ArrayList;

/**
 * Created by mircea.ionita on 11/8/2016.
 */
public class DataService {
    private static DataService instance = new DataService();

    public static DataService getInstance() {
        return instance;
    }

    private DataService() {
    }

    public ArrayList<Restaurants> getCoursesLocationsWithin10MilesOfZip(int zipcode){

        ArrayList<Restaurants> list = new ArrayList<>();

        list.add(new Restaurants(44.433605f, 26.112537f, "MysticTree", "Strada Popa Soare 7, București 030167", "slo"));
        list.add(new Restaurants(44.433408f, 26.109967f, "Jacques Pot", "Strada Zborului 2, București 030595", "slo"));
        list.add(new Restaurants(44.432925f, 26.112242f, "La Scena", "Calea Călărași 55, București 030612", "slo"));

        return list;
    }
}
