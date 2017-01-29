package fastcurrencyconverter.mient.com.fastcurrencyconverter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fastcurrencyconverter.mient.com.fastcurrencyconverter.fragments.FavoriteFragment;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.fragments.MainFragment;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.model.Currency;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.services.DataService;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Currency> todayCurrencies;
    private ArrayList<Currency> favoriteCurrencies = new ArrayList<>();

    FavoriteFragment favoriteFragment;
    MainFragment mainFragment;
    FragmentManager fragmentManager;

    private DataService dataService;

    private String lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        lastUpdate = dateFormat.format(cal.getTime());

        dataService = DataService.getInstance();
        dataService.init(this);

        if (dataService.getLastUpdate().equals(lastUpdate)){
            todayCurrencies = dataService.getLastCurencies();
        }else {
            todayCurrencies = dataService.downloadCurrentValues("latest");
        }

        favoriteCurrencies = dataService.getFavoriteCurrencies();

        fragmentManager = getSupportFragmentManager();
        mainFragment = (MainFragment) fragmentManager.findFragmentById(R.id.container_main);
        if(mainFragment == null){
            mainFragment = MainFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.container_main, mainFragment).commit();
        }

    }

    public void updateList(){
        mainFragment.updateList();
    };

    public void selectCurrency(Currency item){
//        for (int i = 0; i < favoriteCurrencies.size(); i++){
//            if(favoriteCurrencies.get(i).getTag().equals(item.getTag())){
//                favoriteCurrencies.remove(i);
//            }
//        }
        mainFragment.selectCurrency(item);
    }

    public void createFavoriteList(){
        favoriteCurrencies.clear();
        dataService.clearFavCurrencies();
        for (int i = 0; i < todayCurrencies.size(); i++){
            if(todayCurrencies.get(i).isFavorite()){
                favoriteCurrencies.add(todayCurrencies.get(i));
                dataService.insertFavCurrency(todayCurrencies.get(i).getTag());
            }
        }
        loadMainFragment();
    }

    public void loadMainFragment(){
        mainFragment.updateList();
        onBackPressed();
    }

    public void loadFavoriteFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_main, FavoriteFragment.newInstance())
                .addToBackStack(null).commit();
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

    public String getLastUpdate() {
        return lastUpdate;
    }
}
