package com.mient.flaborfit.flaborfit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class SettingsActivity extends AppCompatActivity {

    CheckBox nightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        nightMode = (CheckBox) findViewById(R.id.nightMode);

        nightMode.setChecked(MainActivity.NIGHT_MODE);

        nightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.NIGHT_MODE = nightMode.isChecked();
            }
        });
    }
}
