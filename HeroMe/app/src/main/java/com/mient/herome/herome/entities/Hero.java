package com.mient.herome.herome.entities;

import com.mient.herome.herome.R;
import com.mient.herome.herome.activities.MainActivity;

import java.io.Serializable;

/**
 * Created by Mircea-Ionel on 11/6/2016.
 */

public class Hero implements Serializable{

    private static Hero hero;

    private String name;
    private int logo;
    private MainActivity.POWER_CAUSE powerCause;
    private MainActivity.POWER primaryPower;
    private String primaryPowerStr;
    private int primaryPowerDrawableId;
    private MainActivity.POWER secondaryPower;
    private String secondaryPowerStr;
    private int secondaryPowerDrawableId;
    private String story;

    public static Hero createHero(MainActivity.POWER_CAUSE powerCause, MainActivity.POWER primaryPower, MainActivity.POWER secondaryPower){
        hero = new Hero();
        hero.name = "Ion";
        hero.logo = R.drawable.turtlepower;
        hero.powerCause = MainActivity.POWER_CAUSE.GENETIC;
        hero.primaryPower = MainActivity.POWER.LIGHTNING;
        hero.primaryPowerStr = "Fulgere";
        hero.primaryPowerDrawableId = R.drawable.lightning;
        hero.secondaryPower = MainActivity.POWER.FLIGHT;
        hero.secondaryPowerStr = "Zboara";
        hero.secondaryPowerDrawableId = R.drawable.supermancrest;
        hero.story = "                  Cauze generice, este super jmecher Ion asta.";

        return hero;
    }

    public String getName() {
        return name;
    }

    public int getLogo() {
        return logo;
    }

    public String getPrimaryPowerStr() {
        return primaryPowerStr;
    }

    public int getPrimaryPowerDrawableId() {
        return primaryPowerDrawableId;
    }

    public String getSecondaryPowerStr() {
        return secondaryPowerStr;
    }

    public int getSecondaryPowerDrawableId() {
        return secondaryPowerDrawableId;
    }

    public String getStory() {
        return story;
    }
}
