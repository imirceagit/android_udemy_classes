package com.mient.flaborfit.flaborfit;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_TITLE = "extra.item.title";

    public static final String EXERCISE_WEIGHTS = "Weight lifting";
    public static final String EXERCISE_YOGA = "Yoga";
    public static final String EXERCISE_CARDIO = "Cardio";

    public static boolean NIGHT_MODE = false;

    LinearLayout activityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMain = (LinearLayout) findViewById(R.id.activity_main);

        RelativeLayout weightBtn = (RelativeLayout) findViewById(R.id.weightBtn);
        RelativeLayout yogaBtn = (RelativeLayout) findViewById(R.id.yogaBtn);
        RelativeLayout cardioBtn = (RelativeLayout) findViewById(R.id.cardioBtn);

        weightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDetailActivity(MainActivity.EXERCISE_WEIGHTS);
            }
        });

        yogaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDetailActivity(MainActivity.EXERCISE_YOGA);
            }
        });

        cardioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDetailActivity(MainActivity.EXERCISE_CARDIO);
            }
        });
    }

    private void loadDetailActivity(String exerciseTitle){
        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
        intent.putExtra(EXTRA_ITEM_TITLE, exerciseTitle);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onResume();
        if(MainActivity.NIGHT_MODE){
            activityMain.setBackgroundColor(Color.DKGRAY);
        }else {
            activityMain.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MainActivity.NIGHT_MODE){
            activityMain.setBackgroundColor(Color.DKGRAY);
        }else {
            activityMain.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settings){
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
