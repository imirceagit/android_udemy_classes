package com.mient.mimusicplayer.mimusicplayer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.view.DragEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mient.mimusicplayer.mimusicplayer.fragments.PlaylistsFragment;
import com.mient.mimusicplayer.mimusicplayer.fragments.TracksFragment;
import com.mient.mimusicplayer.mimusicplayer.model.Song;
import com.mient.mimusicplayer.mimusicplayer.services.TracksService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final int PERMISION_READ_EXTERNAL_STORAGE = 123;

    boolean layerOpen = false;

    private RelativeLayout playerlayout;
    private LinearLayout playerActionBar;
    private ImageButton playerFavorite;

    static Point size;

    private static MainActivity mainActivity;

    private TracksFragment tracksFragment = TracksFragment.newInstance();

    private ArrayList<Song> allTracksList = new ArrayList<>();

    TracksService tracksService;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;

        playerlayout = (RelativeLayout) findViewById(R.id.player_layout);
        playerActionBar = (LinearLayout) findViewById(R.id.player_action_bar);
        playerFavorite = (ImageButton) findViewById(R.id.player_favorite);

        Display display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        playerlayout.setTranslationY(height - convertDpToPixel(74, this));

        playerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layerOpen){
                    layerOpen = false;
                    playerlayout.animate().translationY(size.y - convertDpToPixel(74, mainActivity));
                }else {
                    layerOpen = true;
                    playerlayout.animate().translationY(0);
                }
            }
        });

//        playerlayout.setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View v, DragEvent event) {
//
//                switch(event.getAction()) {
//                    case DragEvent.ACTION_DRAG_STARTED:
//                        break;
//
//                    case DragEvent.ACTION_DRAG_ENTERED:
//                        int x_cord = (int) event.getX();
//                        int y_cord = (int) event.getY();
//                        playerlayout.animate().translationY(y_cord);
//                        break;
//
//                    case DragEvent.ACTION_DRAG_EXITED :
//                        x_cord = (int) event.getX();
//                        y_cord = (int) event.getY();
//                        playerlayout.animate().translationY(y_cord);
//                        break;
//
//                    case DragEvent.ACTION_DRAG_LOCATION  :
//                        x_cord = (int) event.getX();
//                        y_cord = (int) event.getY();
//                        playerlayout.animate().translationY(y_cord);
//                        break;
//
//                    case DragEvent.ACTION_DRAG_ENDED   :
//                        break;
//
//                    case DragEvent.ACTION_DROP:
//                        break;
//
//                    default: break;
//                }
//                return true;
//            }
//        });

        tracksService = TracksService.getInstance();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISION_READ_EXTERNAL_STORAGE);
        }else {
            tracksService.init(this);
        }

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

    public void updateAllTracksList(){
        tracksFragment.updateList();
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static MainActivity getMainActivity(){
        return mainActivity;
    }

    public ArrayList<Song> getAllTracksList() {
        return allTracksList;
    }

    public void setAlltracksList(ArrayList<Song> list){
        allTracksList = list;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISION_READ_EXTERNAL_STORAGE: {
                if(grantResults.length > 0 && grantResults[0]  == PackageManager.PERMISSION_GRANTED){
                    tracksService.init(this);
                }else {

                }
            }
        }
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
                    return PlaylistsFragment.newInstance();
                default:
                    return TracksFragment.newInstance();
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
