package com.mient.bootcamplocator.bootcamplocator.model;

/**
 * Created by mircea.ionita on 11/8/2016.
 */

public class Restaurants {

    final String DRAWABLE = "drawable/";

    private float latitude;
    private float longitude;
    private String locationTitle;
    private String locationAddress;
    private String locationImgUrl;

    public Restaurants(float latitude, float longitude, String locationTitle, String locationAddress, String locationImgUrl) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationTitle = locationTitle;
        this.locationAddress = locationAddress;
        this.locationImgUrl = locationImgUrl;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public String getLocationImgUrl() {
        return locationImgUrl;
    }

    public String getImgUri(){ return DRAWABLE + getLocationImgUrl();}
}
