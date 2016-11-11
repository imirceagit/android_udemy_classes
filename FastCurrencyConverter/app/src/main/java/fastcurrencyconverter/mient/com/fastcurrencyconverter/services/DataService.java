package fastcurrencyconverter.mient.com.fastcurrencyconverter.services;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fastcurrencyconverter.mient.com.fastcurrencyconverter.model.Currency;

/**
 * Created by mircea.ionita on 11/10/2016.
 */
public class DataService {

    private String[] tags = {"AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "GBP", "HKD",
        "HRK", "HUF", "IDR", "ILS", "INR", "JPY", "KRW", "MXN", "MYR", "NOK", "NZD", "PHP", "PLN",
        "RON", "RUB", "SEK", "SGD", "THB", "TRY", "USD", "ZAR"};

    private String[] names = {"Australian Dollar", "Bulgarian Lev", "Brazilian Real", "Canadian Dollar",
        "Swiss Franc", "Chinese Yuan", "Czech Koruna", "Danish Krone", "British Pound", "Hong Kong Dollar",
        "Croatian Kuna", "Hungarian Forint", "Indonesian Rupiah", "Israeli Shekel", "Indian rupee",
        "Japanese Yen", "South Korean Won", "Mexican Peso", "Malaysian Ringgit", "Norwegian Krone",
        "New Zealand Dollar", "Philippine Peso", "Polish Zloty", "Romanian Leu", "Russian Ruble",
        "Swedish Krona", "Singapore Dollar", "Thai Baht", "Turkish Lira", "US Dollar", "South African Rand"};

    private final String BASE_URL ="http://api.fixer.io/";

    private static DataService instance;
    private Context context;

    private ArrayList<Currency> todayCurrencies = new ArrayList<>();

    public static DataService getInstance() {
        if(instance == null){
            instance = new DataService();
        }
        return instance;
    }

    private DataService() {

    }

    public void init(Context context){
        this.context = context;
    }

    public ArrayList<Currency> downloadCurrentValues(String uri){

        final String url = BASE_URL + uri;

        final JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        setDataFromResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("DataService", "Error: " + error.getLocalizedMessage());
                    }
                });

        Volley.newRequestQueue(context).add(jsonObjectRequest);

        return todayCurrencies;
    }

    private void setDataFromResponse(JSONObject response){
        try {
            String baseTag = response.getString("base");
            String date = response.getString("date");
            JSONObject rates = response.getJSONObject("rates");

            for (int i = 0; i < tags.length; i++){
                String name = names[i];
                String tag = tags[i];
                double value = rates.getDouble(tag);
                todayCurrencies.add(new Currency(name, tag, value, baseTag, date));
            }

        } catch (JSONException e) {
            Log.v("JSONParser", e.getLocalizedMessage());
        }
    }

    public ArrayList<Currency> getTodayCurrencies(){
        return todayCurrencies;
    }

}
