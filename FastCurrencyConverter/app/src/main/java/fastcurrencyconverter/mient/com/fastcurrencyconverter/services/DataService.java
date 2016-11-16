package fastcurrencyconverter.mient.com.fastcurrencyconverter.services;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
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

import fastcurrencyconverter.mient.com.fastcurrencyconverter.MainActivity;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.model.Currency;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.model.SQLiteHelper;

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

    private SQLiteHelper dbHelper;
    private static DataService instance;

    private MainActivity activity;

    private ArrayList<Currency> todayCurrencies = new ArrayList<>();
    private ArrayList<Currency> favoriteCurrencies = new ArrayList<>();

    public static DataService getInstance() {
        if(instance == null){
            instance = new DataService();
        }
        return instance;
    }

    private DataService() {

    }

    public void init(Activity activity){
        this.activity = (MainActivity) activity;
        dbHelper = new SQLiteHelper(activity);
    }

    public ArrayList<Currency> getFavoriteCurrencies(){
        favoriteCurrencies.clear();
        Cursor cursor = dbHelper.getFavoriteCurencies();
        while (cursor.moveToNext()){
            for (int i = 0; i < todayCurrencies.size(); i++){
                if(cursor.getString(1).toLowerCase().equals(todayCurrencies.get(i).getTag().toLowerCase())){
                    favoriteCurrencies.add(todayCurrencies.get(i));
                    todayCurrencies.get(i).setFavorite(true);
                }
            }
        }
        return favoriteCurrencies;
    }

    public long insertFavCurrency(String tag){
        return dbHelper.insertFavCurrency(tag);
    }

    public void clearFavCurrencies(){
        dbHelper.clearFavoriteTable();
    }

    public  ArrayList<Currency> downloadCurrentValues(String uri){

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

        Volley.newRequestQueue(activity.getBaseContext()).add(jsonObjectRequest);

        return todayCurrencies;
    }

    private void setDataFromResponse(JSONObject response){
        try {
            String baseTag = response.getString("base");
            String date = response.getString("date");
            JSONObject rates = response.getJSONObject("rates");

            todayCurrencies.add(new Currency("Euro", "EUR", 1, baseTag, date, false, 0));

            for (int i = 0; i < tags.length; i++){
                String name = names[i];
                String tag = tags[i];
                double value = rates.getDouble(tag);
                todayCurrencies.add(new Currency(name, tag, value, baseTag, date, false, 0));
            }
            getFavoriteCurrencies();
            activity.updateList();

        } catch (JSONException e) {
            Log.v("JSONParser", e.getLocalizedMessage());
        }
    }

}
