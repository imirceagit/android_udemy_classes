package com.mient.mimusicplayer.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mient.mimusicplayer.R;
import com.mient.mimusicplayer.fragments.PlaylistsFragment;
import com.mient.mimusicplayer.fragments.TracksFragment;
import com.mient.mimusicplayer.model.Constants;
import com.mient.mimusicplayer.model.MusicPlayer;
import com.mient.mimusicplayer.services.PlayerService;
import com.mient.mimusicplayer.model.Playlist;
import com.mient.mimusicplayer.services.ForegroundService;
import com.mient.mimusicplayer.services.MediaContentService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements MusicPlayer{

    private static final String LOG_TAG = "MAIN_ACTIVITY";

    public static MainActivity mainActivity;

    private TracksFragment tracksFragment;
    private PlaylistsFragment playlistsFragment;

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

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    //Utils
    int displayHeight;
    int displayWidth;

    //APP Variables
    public static Playlist defaultPlaylist;
    public static List<Playlist> playlistsList;

    //Media Content
    private MediaContentService mediaContentService;

    //SharedPreferences
    SharedPreferences sharedPref;
    private int preferenceShuffle;
    private int preferenceRepeat;
    private long preferenceLastTrackId;

    private PlayerService playerService;
    private Handler durationHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;

        Intent intent = getIntent();
        if (intent.getAction() == Constants.ACTION.MAIN_ACTION){
            playerService = PlayerService.getInstance();
            durationHandler.postDelayed(updateSeekBarTime, 100);
        } else {
            playerService = PlayerService.getInstance();
        }

        //INIT VARIABLES
        defaultPlaylist = new Playlist("default");
        playlistsList = new ArrayList<>();

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        if(sharedPref.contains(Constants.KEYS.SHUFFLE) && sharedPref.contains(Constants.KEYS.REPEAT) && sharedPref.contains(Constants.KEYS.LAST_TRACK)){
            getPreferences();
        }else {
            initPreferences();
        }

        tracksFragment = TracksFragment.newInstance();
        playlistsFragment = PlaylistsFragment.newInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        options = new BitmapFactory.Options();
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
        playerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        playerLayoutState = Constants.LAYOUT_STATE.COLLAPSED;

        playerActionBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerLayoutState == Constants.LAYOUT_STATE.COLLAPSED){
                    playerLayout.animate().translationY(0);
                    playerLayoutState = Constants.LAYOUT_STATE.EXPANDED;
                    playerActionBarPlay.setVisibility(View.INVISIBLE);
                }else if(playerLayoutState == Constants.LAYOUT_STATE.EXPANDED){
                    playerLayout.animate().translationY(displayHeight - convertDpToPixel(74, mainActivity));
                    playerLayoutState = Constants.LAYOUT_STATE.COLLAPSED;
                    playerActionBarPlay.setVisibility(View.VISIBLE);
                }
            }
        });

        View.OnClickListener playOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerService.getPlayerState() == Constants.PLAYER_STATE.PLAY){
                    pause();
                } else {
                    play();
                }
            }
        };

        playerActionBarPlay.setOnClickListener(playOnClickListener);
        playerPlayButton.setOnClickListener(playOnClickListener);

        playerPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev();
            }
        });

        playerNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });

        playerShuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffle();
            }
        });

        playerRepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeat();
            }
        });

        playerProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    seek(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaContentService = MediaContentService.getInstance();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.PERMISSIONS_ID.READ_EXTERNAL_STORAGE);
        }else {
            mediaContentService.queryAllTracks();
        }
    }

    public void mediaContentComplete(){
        if (playerService != null && playerService.getActivePlaylist() != null){
//            seek((playerService.getProgress() * 100) / (int) playerService.getCurrentTrack().getDuration());
        }else {
            playerService.init(defaultPlaylist, 0, preferenceShuffle, preferenceRepeat);
            updatePlayerUI();
        }
    }

    public void setCurrentTrackPlaying(int position){
        playerService.init(defaultPlaylist, position, preferenceShuffle, preferenceShuffle);
        play();
        updatePlayerUI();
    }

    public void updatePreferences(){
        playerService.updatePreferences(preferenceShuffle, preferenceShuffle);
    }

    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            playerPassedTime.setText(String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(playerService.getProgress()),
                    TimeUnit.MILLISECONDS.toSeconds(playerService.getProgress()) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes(playerService.getProgress()))));

            playerProgressBar.setProgress((playerService.getProgress() * 100) / (int) playerService.getCurrentTrack().getDuration());
            durationHandler.postDelayed(this, 100);
        }
    };

    public void stopProgressBarUpdate(){
        durationHandler.removeCallbacks(updateSeekBarTime);
    }

    public void setProgressOnSeekbar(){
        durationHandler.postDelayed(updateSeekBarTime, 100);
    }

    public void updatePlayerUI(){
        if(playerService.getCurrentTrack().getCoverArtPath() == null){
            playerAlbumArt.setImageResource(R.drawable.ic_music_video_black_48dp);
            playerActionBarAlbumArt.setImageResource(R.drawable.ic_music_video_black_48dp);
        }
        else {
            options.inSampleSize = 2;
            final Bitmap b = BitmapFactory.decodeFile(playerService.getCurrentTrack().getCoverArtPath(), options);
            playerAlbumArt.setImageBitmap(b);
            options.inSampleSize = 8;
            final Bitmap bb = BitmapFactory.decodeFile(playerService.getCurrentTrack().getCoverArtPath(), options);
            playerActionBarAlbumArt.setImageBitmap(bb);
        }

        playerActionBarArtist.setText(playerService.getCurrentTrack().getArtist());
        playerActionBarTitle.setText(playerService.getCurrentTrack().getTitle());
        playerFullTime.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(playerService.getCurrentTrack().getDuration()),
                TimeUnit.MILLISECONDS.toSeconds(playerService.getCurrentTrack().getDuration()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes(playerService.getCurrentTrack().getDuration()))));

        if(playerService.getShuffleMode() == Constants.SHUFFLE_MODE.ON){
            playerShuffleButton.setImageResource(R.drawable.ic_shuffle_orange_48dp);
        }else {
            playerShuffleButton.setImageResource(R.drawable.ic_shuffle_white_48dp);
        }

        switch (playerService.getRepeatMode()){
            case Constants.REPEATE_MODE.ALL: playerRepeatButton.setImageResource(R.drawable.ic_repeat_orange_48dp);
                break;
            case Constants.REPEATE_MODE.ONE: playerRepeatButton.setImageResource(R.drawable.ic_repeat_one_orange_48dp);
                break;
            case Constants.REPEATE_MODE.OFF:
            default:
                playerRepeatButton.setImageResource(R.drawable.ic_repeat_white_48dp);
        }

        switch (playerService.getPlayerState()){
            case Constants.PLAYER_STATE.PLAY:
                playerPlayButton.setImageResource(R.drawable.ic_pause_white_48dp);
                playerActionBarPlay.setImageResource(R.drawable.ic_pause_white_48dp);
                break;
            case Constants.PLAYER_STATE.PAUSE:
            case Constants.PLAYER_STATE.STOP:
            default:
                playerPlayButton.setImageResource(R.drawable.ic_play_arrow_white_48dp);
                playerActionBarPlay.setImageResource(R.drawable.ic_play_arrow_white_48dp);
        }

        if(playerService.getPlayerState() == Constants.PLAYER_STATE.STOP){
            playerProgressBar.setProgress(0);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initPreferences(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Constants.KEYS.SHUFFLE, Constants.SHUFFLE_MODE.OFF);
        editor.putInt(Constants.KEYS.REPEAT, Constants.REPEATE_MODE.OFF);
        editor.putLong(Constants.KEYS.LAST_TRACK, 0);
        editor.commit();
    }

    private void savePreferences(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Constants.KEYS.SHUFFLE, playerService.getShuffleMode());
        editor.putInt(Constants.KEYS.REPEAT, playerService.getRepeatMode());
        editor.putLong(Constants.KEYS.LAST_TRACK, playerService.getCurrentTrack().getTrackId());
        editor.commit();
    }

    private void getPreferences(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        preferenceShuffle = sharedPref.getInt(Constants.KEYS.SHUFFLE, Constants.SHUFFLE_MODE.OFF);
        preferenceRepeat = sharedPref.getInt(Constants.KEYS.REPEAT, Constants.REPEATE_MODE.OFF);
        preferenceLastTrackId = sharedPref.getLong(Constants.KEYS.LAST_TRACK, 0);
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

    public static void displayToast(String message){
        Context context = MainActivity.mainActivity;
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void play() {
        Intent playIntent = new Intent(this, ForegroundService.class);
        if (playerService.getPlayerState() == Constants.PLAYER_STATE.PAUSE){
            playIntent.setAction(Constants.ACTION.RESUME_ACTION);
        } else {
            playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        }
        startService(playIntent);
        playerService.setPlayerState(Constants.PLAYER_STATE.PLAY);
        durationHandler.postDelayed(updateSeekBarTime, 100);
        updatePlayerUI();
    }

    @Override
    public void pause() {
        Intent pauseIntent = new Intent(this, ForegroundService.class);
        pauseIntent.setAction(Constants.ACTION.PAUSE_ACTION);
        startService(pauseIntent);
        playerService.setPlayerState(Constants.PLAYER_STATE.PAUSE);
        durationHandler.removeCallbacks(updateSeekBarTime);
        updatePlayerUI();
    }

    @Override
    public void prev() {
        Intent prevIntent = new Intent(this, ForegroundService.class);
        prevIntent.setAction(Constants.ACTION.PREV_ACTION);
        startService(prevIntent);
        playerService.setPlayerState(Constants.PLAYER_STATE.PLAY);
    }

    @Override
    public void next() {
        Intent nextIntent = new Intent(this, ForegroundService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        startService(nextIntent);
        playerService.setPlayerState(Constants.PLAYER_STATE.PLAY);
    }

    @Override
    public void stop() {
        Intent stopIntent = new Intent(this, ForegroundService.class);
        stopIntent.setAction(Constants.ACTION.STOP_ACTION);
        startService(stopIntent);
        playerService.setPlayerState(Constants.PLAYER_STATE.STOP);
        updatePlayerUI();
    }

    @Override
    public void seek(int progress) {
        Log.v(LOG_TAG, " ============== PROGRESS ++++++ " + progress);
        Intent seekIntent = new Intent(this, ForegroundService.class);
        seekIntent.setAction(Constants.ACTION.SEEK_ACTION);
        seekIntent.putExtra(Constants.KEYS.SEEK_KEY, progress);
        startService(seekIntent);
        updatePlayerUI();
    }

    @Override
    public void shuffle() {
        if (playerService.getShuffleMode() == Constants.SHUFFLE_MODE.ON){
            playerService.setShuffleMode(Constants.SHUFFLE_MODE.OFF);
        }else {
            playerService.setShuffleMode(Constants.SHUFFLE_MODE.ON);
            playerService.setRepeatMode(Constants.REPEATE_MODE.OFF);
        }
        updatePlayerUI();
    }

    @Override
    public void repeat() {
        switch (playerService.getRepeatMode()){
            case Constants.REPEATE_MODE.ALL:
                playerService.setRepeatMode(Constants.REPEATE_MODE.ONE);
                playerService.setShuffleMode(Constants.SHUFFLE_MODE.OFF);
                break;
            case Constants.REPEATE_MODE.ONE:
                playerService.setRepeatMode(Constants.REPEATE_MODE.OFF);
                break;
            case Constants.REPEATE_MODE.OFF:
                playerService.setRepeatMode(Constants.REPEATE_MODE.ALL);
                playerService.setShuffleMode(Constants.SHUFFLE_MODE.OFF);
                break;
            default:
                playerService.setRepeatMode(Constants.REPEATE_MODE.OFF);
        }
        updatePlayerUI();
    }

    @Override
    public void close() {
        //TO DO
        if (playerService.getPlayerState() == Constants.PLAYER_STATE.PLAY){

        }else {
            Intent destroyIntent = new Intent(this, ForegroundService.class);
            destroyIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            startService(destroyIntent);
        }
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
                    return "Tracks";
                case 1:
                    return "Playlists";
            }
            return null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(LOG_TAG, " -------------- ON PAUSE ---------------");
    }

    @Override
    protected void onResume() {
        getPreferences();
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(LOG_TAG, " -------------- ON STOP ---------------");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savePreferences();
        durationHandler.removeCallbacks(updateSeekBarTime);
        close();
    }
}
