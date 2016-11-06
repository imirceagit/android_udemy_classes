package com.mient.flaborfit.flaborfit;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity  {

    LinearLayout activityDetails;
    String exerciseTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        activityDetails = (LinearLayout) findViewById(R.id.activity_details);
        TextView exerciseTitleView = (TextView) findViewById(R.id.exerciseTitleView);
        ImageView exerciseImage = (ImageView) findViewById(R.id.exerciseImage);

        exerciseTitle = getIntent().getStringExtra(MainActivity.EXTRA_ITEM_TITLE);

        exerciseTitleView.setText(exerciseTitle);

        if(exerciseTitle.equalsIgnoreCase(MainActivity.EXERCISE_WEIGHTS) && !MainActivity.NIGHT_MODE){
            exerciseImage.setImageDrawable(getResources().getDrawable(R.drawable.weight, getApplicationContext().getTheme()));
            activityDetails.setBackgroundColor(Color.parseColor("#2ca5f5"));
        }else if(exerciseTitle.equalsIgnoreCase(MainActivity.EXERCISE_YOGA) && !MainActivity.NIGHT_MODE){
            exerciseImage.setImageDrawable(getResources().getDrawable(R.drawable.lotus, getApplicationContext().getTheme()));
            activityDetails.setBackgroundColor(Color.parseColor("#916bcd"));
        }else if(exerciseTitle.equalsIgnoreCase(MainActivity.EXERCISE_CARDIO) && !MainActivity.NIGHT_MODE){
            exerciseImage.setImageDrawable(getResources().getDrawable(R.drawable.heart, getApplicationContext().getTheme()));
            activityDetails.setBackgroundColor(Color.parseColor("#52ad56"));
        }else {
            activityDetails.setBackgroundColor(Color.DKGRAY);
        }

    }

    private void setBackground(String exerciseTitle) {
        if(exerciseTitle.equalsIgnoreCase(MainActivity.EXERCISE_WEIGHTS) && !MainActivity.NIGHT_MODE){
            activityDetails.setBackgroundColor(Color.parseColor("#2ca5f5"));
        }else if(exerciseTitle.equalsIgnoreCase(MainActivity.EXERCISE_YOGA) && !MainActivity.NIGHT_MODE){
            activityDetails.setBackgroundColor(Color.parseColor("#916bcd"));
        }else if(exerciseTitle.equalsIgnoreCase(MainActivity.EXERCISE_CARDIO) && !MainActivity.NIGHT_MODE){
            activityDetails.setBackgroundColor(Color.parseColor("#52ad56"));
        }else {
            activityDetails.setBackgroundColor(Color.DKGRAY);
        }
    }

    @Override
    protected void onRestart() {
        super.onResume();
        setBackground(exerciseTitle);
    }
}
