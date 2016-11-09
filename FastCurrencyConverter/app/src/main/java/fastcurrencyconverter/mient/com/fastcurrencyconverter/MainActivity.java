package fastcurrencyconverter.mient.com.fastcurrencyconverter;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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
            fragmentManager.beginTransaction().add(R.id.container_main, mainFragment).commit();
        }

        DataService.getInstance(this.getApplicationContext()).getRequestQueue().start();

        String url ="http://api.fixer.io/latest";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("MUIE", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("MUIE", error.getLocalizedMessage());
                    }
                });

        DataService.getInstance(this).addToRequestQueue(stringRequest);
    }
}
