package fastcurrencyconverter.mient.com.fastcurrencyconverter.model;

/**
 * Created by Mircea-Ionel on 11/10/2016.
 */

public class Currency {

    private String name;
    private String tag;
    private double value;
    private String baseTag;

    public Currency(String name, String tag, double value, String baseTag) {
        this.name = name;
        this.tag = tag;
        this.value = value;
        this.baseTag = baseTag;
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

    public String getTagLowercase(){
        return tag.toLowerCase();
    }

    public String getBaseTagLowercase(){
        return baseTag.toLowerCase();
    }

}
