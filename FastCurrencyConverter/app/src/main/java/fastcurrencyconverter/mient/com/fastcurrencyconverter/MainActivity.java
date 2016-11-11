package fastcurrencyconverter.mient.com.fastcurrencyconverter;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import fastcurrencyconverter.mient.com.fastcurrencyconverter.fragments.CurrenciesFragment;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.fragments.MainFragment;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.model.Currency;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.services.DataService;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Currency> todayCurrencies = new ArrayList<>();
    private ArrayList<Currency> favoriteCurrencies = new ArrayList<>();

    private DataService dataService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataService = DataService.getInstance();
        dataService.init(this);
        todayCurrencies = dataService.downloadCurrentValues("latest");

        FragmentManager fragmentManager = getSupportFragmentManager();
        MainFragment mainFragment = (MainFragment) fragmentManager.findFragmentById(R.id.container_main);
        if(mainFragment != null){
            mainFragment = MainFragment.newInstance(favoriteCurrencies);
            fragmentManager
                    .beginTransaction()
                    .add(R.id.container_main, mainFragment)
                    .commit();
        }
    }

    public void loadCurrenciesFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_main, CurrenciesFragment.newInstance(todayCurrencies))
                .addToBackStack(null)
                .commit();
    }

}
