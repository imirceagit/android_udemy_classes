package com.mient.currencyconverter.currencyconverter.model;

/**
 * Created by mircea.ionita on 1/19/2017.
 */

public class Currency {

    private String name;
    private String tag;
    private double value;
    private String baseTag;
    private String date;
    private boolean favorite;
    private double result;

    public Currency(String name, String tag, double value, String baseTag, String date, boolean favorite, double result) {
        this.name = name;
        this.tag = tag;
        this.value = value;
        this.baseTag = baseTag;
        this.date = date;
        this.favorite = favorite;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public double getValue() {
        return value;
    }

    public String getBaseTag() {
        return baseTag;
    }

    public String getDate() { return date; }

    public boolean isFavorite() { return favorite; }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public String getUri(){
        return "drawable/" + tag.toLowerCase();
    }

    public String getBaseTagLowercase(){
        return baseTag.toLowerCase();
    }

}