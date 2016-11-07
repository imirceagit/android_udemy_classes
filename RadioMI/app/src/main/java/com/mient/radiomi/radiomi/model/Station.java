package com.mient.radiomi.radiomi.model;

import java.io.Serializable;

/**
 * Created by Mircea-Ionel on 11/7/2016.
 */

public class Station implements Serializable{

    final String DRAWABLE = "drawable/";
    private String stationTitle;
    private String imgUri;
    private String backgroundColor;

    public Station(String stationTitle, String imgUri, String backgroundColor) {
        this.stationTitle = stationTitle;
        this.imgUri = imgUri;
        this.backgroundColor = backgroundColor;
    }

    public String getStationTitle() {
        return stationTitle;
    }

    public String getImgUri() {
        return DRAWABLE + imgUri;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }
}
