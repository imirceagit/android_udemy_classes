
package com.mient.radiomi.radiomi.activities;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mient.radiomi.radiomi.R;
import com.mient.radiomi.radiomi.fragments.DetailsFragment;
import com.mient.radiomi.radiomi.fragments.MainFragment;
import com.mient.radiomi.radiomi.fragments.StationsFragment;
import com.mient.radiomi.radiomi.model.Station;

public class MainActivity extends AppCompatActivity implements MainFragment.OnMainFragmentInteractionListener, StationsFragment.OnStationsFragmentInteractionListener{

    private static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;

        FragmentManager fragmentManager = getSupportFragmentManager();
        MainFragment mainFragment = (MainFragment) fragmentManager.findFragmentById(R.id.main_activity_container);

        if(mainFragment == null){
            mainFragment = MainFragment.newInstance("", "");
            fragmentManager.beginTransaction().add(R.id.main_activity_container, mainFragment).commit();
        }
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public void loadDetailsScreen(Station station){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_activity_container, DetailsFragment.newInstance(station))
                .addToBackStack(null).commit();
    }

    @Override
    public void onMainFragmentInteraction(Uri uri) {

    }

    @Override
    public void onStationsFragmentInteraction(Uri uri) {

    }
}
