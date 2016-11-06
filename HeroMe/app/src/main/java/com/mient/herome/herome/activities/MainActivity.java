package com.mient.herome.herome.activities;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mient.herome.herome.R;
import com.mient.herome.herome.entities.Hero;
import com.mient.herome.herome.fragments.MainFragment;
import com.mient.herome.herome.fragments.PickPowerFragment;
import com.mient.herome.herome.fragments.StoryFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentInteractionListener,
        PickPowerFragment.PickPowerFragmentInteractionListener, StoryFragment.StoryFragmentInteractionListener{

    public static final String HERO_DATA = "HERO";

    public static enum POWER_CAUSE {
        ACCIDENT, GENETIC, BORN_WITH
    };

    public static enum POWER {
        TURTLE_POWER, LIGHTNING, FLIGHT, WEB_SLINGING, LASER_VISION, SUPER_STRENGHT
    };

    private MainActivity.POWER_CAUSE powerCause;
    private MainActivity.POWER primaryPower;
    private MainActivity.POWER secondaryPower;

    private Hero hero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = new MainFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public void onMainFragmentInteraction(MainActivity.POWER_CAUSE powerCause) {
        this.powerCause = powerCause;
        PickPowerFragment pickPowerFragment = new PickPowerFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pickPowerFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onPickPowerFragmentInteraction(List<POWER> powers) {
        this.primaryPower = powers.get(0);
        this.secondaryPower = powers.get(1);
        hero = Hero.createHero(powerCause, primaryPower, secondaryPower);
        StoryFragment storyFragment = new StoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(MainActivity.HERO_DATA, hero);
        storyFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, storyFragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onStoryFragmentInteraction(String data) {
        MainFragment mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mainFragment)
                .addToBackStack(null).commit();
    }
}
