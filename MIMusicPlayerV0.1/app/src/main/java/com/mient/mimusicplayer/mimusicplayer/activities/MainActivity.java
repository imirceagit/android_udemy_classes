package com.mient.mimusicplayer.mimusicplayer.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mient.mimusicplayer.mimusicplayer.fragment.PlaylistsFragment;
import com.mient.mimusicplayer.mimusicplayer.fragment.TracksFragment;
import com.mient.mimusicplayer.mimusicplayer.model.Constants;
import com.mient.mimusicplayer.mimusicplayer.R;
import com.mient.mimusicplayer.mimusicplayer.model.Track;
import com.mient.mimusicplayer.mimusicplayer.services.ForegroundService;
import com.mient.mimusicplayer.mimusicplayer.services.MediaContentService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = "MAIN_ACTIVITY";

    public static MainActivity mainActivity;

    public static ArrayList<Track> allTracksList = new ArrayList<>();

    //MainActivity Widgets - player Action Bar
    private BitmapFactory.Options options;
    private LinearLayout playerLayout;
    private int playerLayoutState;
    private ConstraintLayout playerActionBar;
    private ImageView playerActionBarAlbumArt;
    private TextView playerActionBarArtist;
    private TextView playerActionBarTitle;
    private ImageButton playerActionBarPlay;

    //MainActivity Widgets - player
    private ImageView playerAlbumArt;
    private ImageButton playerShuffleButton;
    private ImageButton playerPrevButton;
    private ImageButton playerPlayButton;
    private ImageButton playerNextButton;
    private ImageButton playerRepeatButton;
    private SeekBar playerProgressBar;
    private TextView playerPassedTime;
    private TextView playerFullTime;

    //FRAGMENTS
    private TracksFragment tracksFragment;
    private PlaylistsFragment playlistsFragment;

    //Tabs Utils
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    //Utils
    int displayHeight;
    int displayWidth;

    //Media Content
    private MediaContentService mediaContentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

        tracksFragment = TracksFragment.newInstance();
        playlistsFragment = PlaylistsFragment.newInstance();

        getDisplaySize();

        playerLayout = (LinearLayout) findViewById(R.id.player_layout);
        playerActionBar = (ConstraintLayout) findViewById(R.id.player_action_bar);
        playerActionBarAlbumArt = (ImageView) findViewById(R.id.player_action_bar_album_art);
        playerActionBarArtist = (TextView) findViewById(R.id.player_action_bar_artist);
        playerActionBarTitle = (TextView) findViewById(R.id.player_action_bar_title);
        playerActionBarPlay = (ImageButton) findViewById(R.id.player_action_bar_play);

        playerPassedTime = (TextView) findViewById(R.id.player_passed_time);
        playerAlbumArt = (ImageView) findViewById(R.id.player_album_art);
        playerFullTime = (TextView) findViewById(R.id.player_full_time);
        playerShuffleButton = (ImageButton) findViewById(R.id.player_shuffle_button);
        playerPrevButton = (ImageButton) findViewById(R.id.player_prev_button);
        playerPlayButton = (ImageButton) findViewById(R.id.player_play_button);
        playerNextButton = (ImageButton) findViewById(R.id.player_next_button);
        playerRepeatButton = (ImageButton) findViewById(R.id.player_repeat_button);
        playerProgressBar = (SeekBar) findViewById(R.id.player_progress_bar);
        playerProgressBar.getProgressDrawable().setColorFilter( Color.parseColor("#FF601C"), android.graphics.PorterDuff.Mode.SRC_IN);
        playerProgressBar.setScaleY(2f);

        playerLayout.setTranslationY(displayHeight - convertDpToPixel(74, mainActivity));
        playerLayoutState = Constants.LAYOUT_STATE.COLLAPSED;

        mediaContentService = MediaContentService.getInstance();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSIONS_ID.READ_EXTERNAL_STORAGE);
        }else {
            Log.v(LOG_TAG, "QUERY");
            mediaContentService.queryAllTracks();
        }

//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent startIntent = new Intent(MainActivity.this, ForegroundService.class);
//                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
//                startService(startIntent);
//            }
//        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case Constants.PERMISSIONS_ID.READ_EXTERNAL_STORAGE: {
                if(grantResults.length > 0 && grantResults[0]  == PackageManager.PERMISSION_GRANTED){
                    mediaContentService.queryAllTracks();
                }else {

                }
            }
        }
    }

    public static void displayToast(String message){
        Context context = MainActivity.mainActivity;
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void getDisplaySize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        displayHeight = size.y;
        displayWidth = size.x;
    }

    private float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    private float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    return tracksFragment;
                case 1:
                    return playlistsFragment;
                default:
                    return tracksFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "TRACKS";
                case 1:
                    return "PLAYLISTS";
            }
            return null;
        }
    }
}
