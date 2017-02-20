package com.mient.mimusicplayer.activities;

import android.Manifest;
import android.content.Context;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import com.mient.mimusicplayer.model.Playlist;
import com.mient.mimusicplayer.services.MediaContentService;
import com.mient.mimusicplayer.services.MediaPlayerService;
import com.mient.mimusicplayer.services.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

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

    //Player
    private Player player;

    //Media Content
    private MediaContentService mediaContentService;

    //SharedPreferences
    SharedPreferences sharedPref;
    private int preferenceShuffle;
    private int preferenceRepeat;
    private long preferenceLastTrackId;

    private MediaPlayerService mediaPlayerService;
    private Handler durationHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        setContentView(R.layout.activity_main);

        mediaPlayerService = MediaPlayerService.getInstance();

        //INIT VARIABLES
        defaultPlaylist = new Playlist("default");
        playlistsList = new ArrayList<Playlist>();
        player = new Player();
        player.setPlayerState(Constants.PLAYER_STATE.STOP);

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
        playerLayout.bringToFront();
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
                if (player.getPlayerState() == Constants.PLAYER_STATE.PLAY){
                    durationHandler.postDelayed(updateSeekBarTime, 100);
                    player.pause();
                } else {
                    durationHandler.removeCallbacks(updateSeekBarTime);
                    player.play();
                }
            }
        };

        playerActionBarPlay.setOnClickListener(playOnClickListener);
        playerPlayButton.setOnClickListener(playOnClickListener);

        playerPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.prev();
            }
        });

        playerNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.next();
            }
        });

        playerShuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.shuffle();
            }
        });

        playerRepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.repeat();
            }
        });

        playerProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    player.seek(i);
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
        if (defaultPlaylist.getTrackCount() >= 0){
            defaultPlaylist.setCurrentPosition(0);
        }
        durationHandler.postDelayed(updateSeekBarTime, 100);
        player.initPlayer(defaultPlaylist, true);
        updatePlayerUI();
    }

    public void setCurrentTrackPlaying(int position){
        defaultPlaylist.setCurrentPosition(position);
        durationHandler.postDelayed(updateSeekBarTime, 100);
        player.initPlayer(defaultPlaylist, false);
        updatePlayerUI();
    }

    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            playerPassedTime.setText(String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(mediaPlayerService.getCurrentProgress()),
                    TimeUnit.MILLISECONDS.toSeconds(mediaPlayerService.getCurrentProgress()) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes(mediaPlayerService.getCurrentProgress()))));

            playerProgressBar.setProgress((mediaPlayerService.getCurrentProgress() * 100) / (int) player.getCurrentTrack().getDuration());

            if (mediaPlayerService.getCurrentProgress() < 2){
                player.setCurrentTrack(mediaPlayerService.getCurrentPlaying());
                updatePlayerUI();
            }

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
        if(player.getCurrentTrack().getCoverArtPath() == null){
            playerAlbumArt.setImageResource(R.drawable.ic_music_video_black_48dp);
            playerActionBarAlbumArt.setImageResource(R.drawable.ic_music_video_black_48dp);
        }
        else {
            options.inSampleSize = 2;
            final Bitmap b = BitmapFactory.decodeFile(player.getCurrentTrack().getCoverArtPath(), options);
            playerAlbumArt.setImageBitmap(b);
            options.inSampleSize = 8;
            final Bitmap bb = BitmapFactory.decodeFile(player.getCurrentTrack().getCoverArtPath(), options);
            playerActionBarAlbumArt.setImageBitmap(bb);
        }

        playerActionBarArtist.setText(player.getCurrentTrack().getArtist());
        playerActionBarTitle.setText(player.getCurrentTrack().getTitle());
        playerFullTime.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(player.getCurrentTrack().getDuration()),
                TimeUnit.MILLISECONDS.toSeconds(player.getCurrentTrack().getDuration()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                toMinutes(player.getCurrentTrack().getDuration()))));

        if(player.getShuffleMode() == Constants.SHUFFLE_MODE.ON){
            playerShuffleButton.setImageResource(R.drawable.ic_shuffle_orange_48dp);
        }else {
            playerShuffleButton.setImageResource(R.drawable.ic_shuffle_white_48dp);
        }

        switch (player.getRepeatMode()){
            case Constants.REPEATE_MODE.ALL: playerRepeatButton.setImageResource(R.drawable.ic_repeat_orange_48dp);
                break;
            case Constants.REPEATE_MODE.ONE: playerRepeatButton.setImageResource(R.drawable.ic_repeat_one_orange_48dp);
                break;
            case Constants.REPEATE_MODE.OFF:
            default:
                playerRepeatButton.setImageResource(R.drawable.ic_repeat_white_48dp);
        }

        switch (player.getPlayerState()){
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

        if(player.getPlayerState() == Constants.PLAYER_STATE.STOP){
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
        editor.putInt(Constants.KEYS.SHUFFLE, player.getShuffleMode());
        editor.putInt(Constants.KEYS.REPEAT, player.getRepeatMode());
        editor.putLong(Constants.KEYS.LAST_TRACK, player.getCurrentTrack().getTrackId());
        editor.commit();
    }

    private void getPreferences(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        preferenceShuffle = sharedPref.getInt(Constants.KEYS.SHUFFLE, Constants.SHUFFLE_MODE.OFF);
        preferenceRepeat = sharedPref.getInt(Constants.KEYS.REPEAT, Constants.REPEATE_MODE.OFF);
        preferenceLastTrackId = sharedPref.getLong(Constants.KEYS.LAST_TRACK, 0);

        player.setShuffleMode(preferenceShuffle);
        player.setRepeatMode(preferenceRepeat);

        if (defaultPlaylist.getTrackList() != null && defaultPlaylist.getTrackCount() > 0 && preferenceLastTrackId > 0){
            mediaContentComplete();
        }
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
    }

    @Override
    protected void onResume() {
        getPreferences();
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savePreferences();
        durationHandler.removeCallbacks(updateSeekBarTime);
        player.onDestroy();
    }
}
