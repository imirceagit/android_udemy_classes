package com.mient.mimusicplayer.mimusicplayer;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mient.mimusicplayer.mimusicplayer.fragments.PlaylistsFragment;
import com.mient.mimusicplayer.mimusicplayer.fragments.TracksFragment;
import com.mient.mimusicplayer.mimusicplayer.model.Player;
import com.mient.mimusicplayer.mimusicplayer.model.Song;
import com.mient.mimusicplayer.mimusicplayer.services.TracksService;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Permisions
    final int PERMISION_READ_EXTERNAL_STORAGE = 123;

    //Enums
    public enum LayoutState {
        COLLAPSED, EXPANDED
    }

    public static MainActivity mainActivity;

    public static ArrayList<Song> allTracksList = new ArrayList<>();

    //MainActivity Widgets - player Action Bar
    private LinearLayout playerLayout;
    private LayoutState playerLayoutState;
    private ConstraintLayout playerActionBar;
    private TextView playerActionBarArtist;
    private TextView playerActionBarTitle;
    private ImageButton playerActionBarPlay;

    //MainActivity Widgets - player
    private ImageButton playerShuffleButton;
    private ImageButton playerPrevButton;
    private ImageButton playerPlayButton;
    private ImageButton playerNextButton;
    private ImageButton playerRepeatButton;
    private SeekBar playerProgressBar;
    private TextView playerPassedTime;
    private TextView playerFullTime;

    //Contentresolver for Media
    private TracksService tracksService;

    //Player Instance
    private Player musicPlayer;

    private TracksFragment tracksFragment;

    //SharedPreferences
    SharedPreferences sharedPref;
    private boolean playerShuffle;
    private int playerRepeat;
    private long playerAudioId;

    //MusicPlayer
    private Handler durationHandler = new Handler();
    private static ArrayList<Song> currentSongList;
    private static int currentSongPositionInCurrentSongList;

    //Tabs Utils
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    //Utils
    int displayHeight;
    int displayWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        getDisplaySize();
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        musicPlayer = Player.getInstance();

        tracksFragment = TracksFragment.newInstance();

        if(sharedPref.contains(getString(R.string.mi_music_player_shuffle)) && sharedPref.contains(getString(R.string.mi_music_player_repeat)) && sharedPref.contains(getString(R.string.mi_music_player_last_track))){
            getPreferences();
        }else {
            initPreferences();
        }

        musicPlayer.init(allTracksList, 0, 0, Player.PLAYER_STOP, playerShuffle, playerRepeat);

        playerLayout = (LinearLayout) findViewById(R.id.player_layout);
        playerActionBar = (ConstraintLayout) findViewById(R.id.player_action_bar);
        playerActionBarArtist = (TextView) findViewById(R.id.player_action_bar_artist);
        playerActionBarTitle = (TextView) findViewById(R.id.player_action_bar_title);
        playerActionBarPlay = (ImageButton) findViewById(R.id.player_action_bar_play);

        playerPassedTime = (TextView) findViewById(R.id.player_passed_time);
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
        playerLayoutState = LayoutState.COLLAPSED;

        playerProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    musicPlayer.seekMusic((int) (musicPlayer.getMusicTime() * progress/100));
                    playerPassedTime.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes((musicPlayer.getMusicTime() * progress/100)),
                            TimeUnit.MILLISECONDS.toSeconds((musicPlayer.getMusicTime() * progress/100)) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((musicPlayer.getMusicTime() * progress/100)))));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        playerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        playerActionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerLayoutState == LayoutState.COLLAPSED){
                    playerLayout.animate().translationY(0);
                    playerLayoutState = LayoutState.EXPANDED;
                    playerActionBarPlay.setVisibility(View.INVISIBLE);
                }else if(playerLayoutState == LayoutState.EXPANDED){
                    playerLayout.animate().translationY(displayHeight - convertDpToPixel(74, mainActivity));
                    playerLayoutState = LayoutState.COLLAPSED;
                    playerActionBarPlay.setVisibility(View.VISIBLE);
                }
            }
        });

        playerShuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.shuffle();
            }
        });

        playerRepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.repeat();
            }
        });

        playerPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.play();
                if(musicPlayer.getPlayerState() == Player.PLAYER_PAUSE){
                    durationHandler.removeCallbacks(updateSeekBarTime);
                }else if(musicPlayer.getPlayerState() == Player.PLAYER_PLAY){
                    durationHandler.postDelayed(updateSeekBarTime, 100);
                }
            }
        });

        playerPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.prev();
            }
        });

        playerNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.next();
            }
        });

        playerActionBarPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.play();
                if(musicPlayer.getPlayerState() == Player.PLAYER_PAUSE){
                    durationHandler.removeCallbacks(updateSeekBarTime);
                }else if(musicPlayer.getPlayerState() == Player.PLAYER_PLAY){
                    durationHandler.postDelayed(updateSeekBarTime, 100);
                }
            }
        });

//        playerActionBar.setOnTouchListener(new View.OnTouchListener() {
//
//            float xCoOrdinate;
//            float yCoOrdinate;
//
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                switch (event.getActionMasked()) {
//                    case MotionEvent.ACTION_DOWN:
//                        xCoOrdinate = playerLayout.getX() - event.getRawX();
//                        yCoOrdinate = playerLayout.getY() - event.getRawY();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        if(playerLayoutState == LayoutState.COLLAPSED && ((event.getRawY() + yCoOrdinate) < displayHeight / 2)){
//                            playerLayout.animate().translationY(0);
//                            playerLayoutState = LayoutState.EXPANDED;
//                            playerActionBarPlay.setVisibility(View.INVISIBLE);
//                        }
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        if(playerLayoutState == LayoutState.COLLAPSED && ((event.getRawY() + yCoOrdinate) > displayHeight / 2)){
//                            playerLayout.animate().translationY(displayHeight - convertDpToPixel(74, mainActivity));
//                            playerLayoutState = LayoutState.COLLAPSED;
//                            playerActionBarPlay.setVisibility(View.VISIBLE);
//                        }
//                    default:
//                        return false;
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

        updatePlayerUI();
    }

    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            playerPassedTime.setText(String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(musicPlayer.getProgress()),
                    TimeUnit.MILLISECONDS.toSeconds(musicPlayer.getProgress()) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes(musicPlayer.getProgress()))));
            playerProgressBar.setProgress((musicPlayer.getProgress() * 100) / (int) musicPlayer.getMusicTime());
            durationHandler.postDelayed(this, 100);
        }
    };

    public void stopProgressBarUpdate(){
        durationHandler.removeCallbacks(updateSeekBarTime);
    }

    public void setProgressOnSeekbar(){
        durationHandler.postDelayed(updateSeekBarTime, 100);
    }

    private void setTracksToPlayer(){
        if(currentSongList != null && currentSongList.size() > 0 && currentSongPositionInCurrentSongList >= 0){
            musicPlayer.setPlayingList(currentSongList);
            musicPlayer.setCurrentPlaying(currentSongPositionInCurrentSongList);
            musicPlayer.setProgress(0);
            musicPlayer.setPlayerState(Player.PLAYER_PAUSE);
        }
        musicPlayer.play();
        updatePlayerUI();
    }

    public void setCurrentTrack(ArrayList<Song> songList, int position){
        currentSongList = songList;
        currentSongPositionInCurrentSongList = position;
        setTracksToPlayer();
    }

    public void updatePlayerUI(){

        playerActionBarArtist.setText(musicPlayer.getCurrentPlayingForUI().getArtist());
        playerActionBarTitle.setText(musicPlayer.getCurrentPlayingForUI().getTitle());
        playerFullTime.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(musicPlayer.getCurrentPlayingForUI().getTime()),
                TimeUnit.MILLISECONDS.toSeconds(musicPlayer.getCurrentPlayingForUI().getTime()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes(musicPlayer.getCurrentPlayingForUI().getTime()))));

        if(musicPlayer.isShuffle()){
            playerShuffleButton.setImageResource(R.drawable.ic_shuffle_orange_48dp);
        }else {
            playerShuffleButton.setImageResource(R.drawable.ic_shuffle_white_48dp);
        }

        switch (musicPlayer.getRepeat()){
            case Player.REPEAT_ALL: playerRepeatButton.setImageResource(R.drawable.ic_repeat_orange_48dp);
                break;
            case Player.REPEAT_ONE: playerRepeatButton.setImageResource(R.drawable.ic_repeat_one_orange_48dp);
                break;
            case Player.REPEAT_OFF:
            default:
                playerRepeatButton.setImageResource(R.drawable.ic_repeat_white_48dp);
        }

        switch (musicPlayer.getPlayerState()){
            case Player.PLAYER_PLAY:
                playerPlayButton.setImageResource(R.drawable.ic_pause_white_48dp);
                playerActionBarPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                break;
            case Player.PLAYER_PAUSE:
            case Player.PLAYER_STOP:
            default:
                playerPlayButton.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                playerActionBarPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
        }

        if(musicPlayer.getPlayerState() == Player.PLAYER_STOP){
            playerProgressBar.setProgress(0);
        }
    }

    private void initPreferences(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.mi_music_player_shuffle), false);
        editor.putInt(getString(R.string.mi_music_player_repeat), Player.REPEAT_OFF);
        editor.putLong(getString(R.string.mi_music_player_last_track), 0);
        editor.commit();
    }

    private void savePreferences(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.mi_music_player_shuffle), musicPlayer.isShuffle());
        editor.putInt(getString(R.string.mi_music_player_repeat), musicPlayer.getRepeat());
        editor.putLong(getString(R.string.mi_music_player_last_track), musicPlayer.getLastTrackAudioId());
        editor.commit();
    }

    private void getPreferences(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        playerShuffle = sharedPref.getBoolean(getString(R.string.mi_music_player_shuffle), false);
        playerRepeat = sharedPref.getInt(getString(R.string.mi_music_player_repeat), Player.REPEAT_OFF);
        playerAudioId = sharedPref.getLong(getString(R.string.mi_music_player_last_track), 0);
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
        }else {
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
