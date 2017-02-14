package com.mient.signin;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mircea.ionita on 2/14/2017.
 */

public class LogedEntity {

    private static LogedEntity instance;

    private Activity activity;
    private String uid;
    private String profileName;
    private String accessToken;

    public static LogedEntity getInstance(){
        if (instance == null){
            instance = new LogedEntity();
        }
        return instance;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void saveAccessToken() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("FBtoken", getAccessToken());
        editor.apply();
    }

    public String getToken() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        String token = sp.getString("FBtoken", null);
        setAccessToken(token);
        return token;
    }
}
