package fastcurrencyconverter.mient.com.fastcurrencyconverter;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fastcurrencyconverter.mient.com.fastcurrencyconverter.fragments.CurrenciesFragment;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.fragments.MainFragment;
import fastcurrencyconverter.mient.com.fastcurrencyconverter.services.DataService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MainFragment mainFragment = (MainFragment) fragmentManager.findFragmentById(R.id.container_main);
        if(mainFragment != null){
            mainFragment = MainFragment.newInstance();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.container_main, mainFragment)
                    .commit();
        }
    }

    public void loadCurrenciesFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_main, CurrenciesFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}
