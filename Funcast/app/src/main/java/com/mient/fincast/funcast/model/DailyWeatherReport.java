package com.mient.fincast.funcast.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mircea.ionita on 11/9/2016.
 */

public class DailyWeatherReport {

    private String cityName;
    private String country;
    private int currentTemp;
    private int maxtemp;
    private int minTemp;
    private String weather;
    private String formatedDate;

    public DailyWeatherReport(String cityName, String country, int currentTemp, int maxtemp, int minTemp, String weather, String date) {
        this.cityName = cityName;
        this.country = country;
        this.currentTemp = currentTemp;
        this.maxtemp = maxtemp;
        this.minTemp = minTemp;
        this.weather = weather;
        this.formatedDate = formatDate(date);
    }

    public String formatDate(String rawDate){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try {
            calendar.setTime(sdf.parse(rawDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date date = new Date();
        return sdf.format(date);
    }

    public String getCityName() {
        return cityName;
    }

    public String getCountry() {
        return country;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public int getMaxtemp() {
        return maxtemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public String getWeather() {
        return weather;
    }

    public String getFormatedDate() {
        return formatedDate;
    }
}
