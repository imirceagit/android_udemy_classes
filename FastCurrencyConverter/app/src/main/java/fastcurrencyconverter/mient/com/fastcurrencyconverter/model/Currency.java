package fastcurrencyconverter.mient.com.fastcurrencyconverter.model;

/**
 * Created by Mircea-Ionel on 11/10/2016.
 */

public class Currency {

    private String name;
    private String tag;
    private double value;
    private String baseTag;
    private String date;
    private boolean favorite;

    public Currency(String name, String tag, double value, String baseTag, String date, boolean favorite) {
        this.name = name;
        this.tag = tag;
        this.value = value;
        this.baseTag = baseTag;
        this.date = date;
        this.favorite = favorite;
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

    public String getUri(){
        return "drawable/" + tag.toLowerCase();
    }

    public String getBaseTagLowercase(){
        return baseTag.toLowerCase();
    }

}
