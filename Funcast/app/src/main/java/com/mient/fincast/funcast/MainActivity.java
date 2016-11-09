package com.mient.fincast.funcast;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.mient.fincast.funcast.model.DailyWeatherReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    private final int PERMISSION_LOCATION_CODE = 111;

    final String URL_BASE = "http://api.openweathermap.org/data/2.5/forecast";
    final String URL_LAT = "?lat=";
    final String URL_LON = "&lon=";
    final String URL_UNITS = "&units=metric";
    final String URL_API_KEY = "&APPID=e26d76af3ca9f5d912c596ebad41b9d2";

    private GoogleApiClient mGoogleApiClient;

    private ArrayList<DailyWeatherReport> dailyWeatherReports = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void downloadWeatherDate(Location location){
        final String url = URL_BASE + URL_LAT + location.getLatitude() + URL_LON + location.getLongitude() + URL_UNITS + URL_API_KEY;

        final JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    try{

                        JSONObject city = response.getJSONObject("city");
                        String cityName = city.getString("name");
                        String country = city.getString("country");

                        JSONArray list = response.getJSONArray("list");
                        for(int i = 0; i < 5; i++){
                            JSONObject obj = list.getJSONObject(i);
                            JSONObject main = obj.getJSONObject("main");
                            Double currTemp = main.getDouble("temp");
                            Double maxTemp = main.getDouble("temp_max");
                            Double minTemp = main.getDouble("temp_min");

                            JSONArray weatherR = obj.getJSONArray("weather");
                            JSONObject objR = weatherR.getJSONObject(0);
                            String weather = objR.getString("weather");
                            String rawDate = obj.getString("dt_txt");

                            dailyWeatherReports
                                    .add(new DailyWeatherReport(cityName, country, currTemp.intValue(), maxTemp.intValue(), minTemp.intValue(), weather, rawDate));
                        }

                    }catch (JSONException e){

                    }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("FUN", "Err: " + error.getLocalizedMessage());
                    }
                });

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_LOCATION_CODE);
        }else {
            startLocationService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_LOCATION_CODE: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startLocationService();
                }else {
                    Toast.makeText(this, "Location permission denied. To use Funcast you have to grant locaton permission.", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }

    private void startLocationService(){
        try{
            LocationRequest request = LocationRequest.create().setPriority(LocationRequest.PRIORITY_LOW_POWER);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,request,this);
        }catch (SecurityException e){

        }
    };

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        downloadWeatherDate(location);
    }
}
