package fastcurrencyconverter.mient.com.fastcurrencyconverter;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import fastcurrencyconverter.mient.com.fastcurrencyconverter.fragments.FavoriteFragment;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.model.Currency;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.services.DataService;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Currency> todayCurrencies;
    private ArrayList<Currency> favoriteCurrencies;

    FavoriteFragment favoriteFragment;

    private DataService dataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataService = DataService.getInstance();
        dataService.init(this);
        todayCurrencies = dataService.downloadCurrentValues("latest");
        favoriteCurrencies = dataService.getFavoriteCurrencies();

        FragmentManager fragmentManager = getSupportFragmentManager();
        favoriteFragment = (FavoriteFragment) fragmentManager.findFragmentById(R.id.container_main);
        if(favoriteFragment == null){
            favoriteFragment = FavoriteFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.container_main, favoriteFragment).commit();
        }
    }

    public void updateList(){
        favoriteFragment.updateList();
    };

    public void createFavoriteList(){
        favoriteCurrencies.clear();
        for (int i = 0; i < todayCurrencies.size(); i++){
            if(todayCurrencies.get(i).isFavorite()){
                favoriteCurrencies.add(todayCurrencies.get(i));
                Log.v("FAVORITES", todayCurrencies.get(i).getTag());
            }
        }
    }

    public ArrayList<Currency> getTodayCurrencies() {
        return todayCurrencies;
    }

    public ArrayList<Currency> getFavoriteCurrencies() {
        return favoriteCurrencies;
    }

    public void setFavoriteCurrencies(ArrayList<Currency> favoriteCurrencies) {
        this.favoriteCurrencies = favoriteCurrencies;
    }
}
