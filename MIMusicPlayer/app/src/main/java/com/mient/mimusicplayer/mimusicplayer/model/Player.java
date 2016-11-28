package com.mient.mimusicplayer.mimusicplayer.model;

import android.media.MediaPlayer;
import android.util.Log;

import com.mient.mimusicplayer.mimusicplayer.MainActivity;
import com.mient.mimusicplayer.mimusicplayer.services.MediaPlayerService;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mircea.ionita on 11/24/2016.
 */
public class Player{

    private static Player instance;

    public static final int PLAYER_PLAY = 0;
    public static final int PLAYER_PAUSE = 1;
    public static final int PLAYER_STOP = 2;

    public static final int REPEAT_OFF = 0;
    public static final int REPEAT_ALL = 1;
    public static final int REPEAT_ONE = 2;

    private MainActivity activity = MainActivity.mainActivity;
    private MediaPlayerService mediaPlayerService;

    private ArrayList<Song> playingList;
    private int currentPlaying;
    private int progress;
    private int playerState;
    private boolean shuffle;
    private int repeat;

    public static Player getInstance() {
        if(instance == null){
            instance = new Player();
        }
        return instance;
    }

    private Player() {
    }

    public void init(ArrayList<Song> playingList, int currentPlaying, int progress, int playerState, boolean shuffle, int repeat) {
        mediaPlayerService = new MediaPlayerService();
        mediaPlayerService.setPlayerHelper(instance);
        this.playingList = playingList;
        this.currentPlaying = currentPlaying;
        this.progress = progress;
        this.playerState = playerState;
        this.shuffle = shuffle;
        this.repeat = repeat;
    }

    public Song getCurrentPlayingForUI(){
        return playingList.get(currentPlaying);
    }

    public void play(){
        switch (getPlayerState()){
            case Player.PLAYER_PLAY:
                setPlayerState(Player.PLAYER_PAUSE);
                pause();
                break;
            case Player.PLAYER_PAUSE:
                setPlayerState(Player.PLAYER_PLAY);
                mediaPlayerService.play(playingList.get(currentPlaying));
                break;
            case Player.PLAYER_STOP:
                setPlayerState(Player.PLAYER_PLAY);
                mediaPlayerService.play(playingList.get(currentPlaying));
                break;
            default:
                setPlayerState(Player.PLAYER_STOP);
                stop();
        }
        activity.updatePlayerUI();
    }

    public void playCurrent(){
        mediaPlayerService.play(playingList.get(currentPlaying));
        activity.updatePlayerUI();
    }


    public void pause(){
        mediaPlayerService.pause();
    }

    public void stop(){
        setPlayerState(PLAYER_STOP);
        mediaPlayerService.stop();
        activity.updatePlayerUI();
    }

    public void prev(){
        Random r = new Random();
        if(isShuffle()){
            int rand = r.nextInt(playingList.size());
            currentPlaying = rand;
        }else if(currentPlaying > 0 ){
            currentPlaying--;
        }else if (currentPlaying == 0){
            currentPlaying = playingList.size() - 1;
        }
        mediaPlayerService.play(playingList.get(currentPlaying));
        setPlayerState(PLAYER_PLAY);
        activity.updatePlayerUI();
    }

    public void next(){
        Random r = new Random();
        if(isShuffle()){
            int rand = r.nextInt(playingList.size());
            currentPlaying = rand;
        }else if(currentPlaying < playingList.size() - 1 ){
            currentPlaying++;
        }else if (currentPlaying == playingList.size() - 1){
            currentPlaying = 0;
        }

        mediaPlayerService.play(playingList.get(currentPlaying));
        setPlayerState(PLAYER_PLAY);
        activity.updatePlayerUI();
    }

    public void shuffle(){
        if (isShuffle()){
            setShuffle(false);
        }else{
            setShuffle(true);
            setRepeat(Player.REPEAT_OFF);
        }
        activity.updatePlayerUI();
    }

    public void repeat(){
        switch (getRepeat()){
            case Player.REPEAT_ALL:
                setRepeat(Player.REPEAT_ONE);
                setShuffle(false);
                break;
            case Player.REPEAT_ONE:
                setRepeat(Player.REPEAT_OFF);
                break;
            case Player.REPEAT_OFF:
                setRepeat(Player.REPEAT_ALL);
                setShuffle(false);
                break;
            default:
                setRepeat(Player.REPEAT_OFF);
        }
        activity.updatePlayerUI();
    }

    public void seekMusic(int progress){
        setProgress(progress);
        mediaPlayerService.seek(progress);

    }

    public long getMusicTime(){
        return playingList.get(currentPlaying).getTime();
    }


    public long getLastTrackAudioId(){
        long audioId = 0;
        return audioId;
    }

    public static void setInstance(Player instance) {
        Player.instance = instance;
    }

    public ArrayList<Song> getPlayingList() {
        return playingList;
    }

    public void setPlayingList(ArrayList<Song> playingList) {
        this.playingList = playingList;
    }

    public int getCurrentPlaying() {
        return currentPlaying;
    }

    public void setCurrentPlaying(int currentPlaying) {
        this.currentPlaying = currentPlaying;
    }

    public int getProgress() {
        progress = mediaPlayerService.getProgress();
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getPlayerState() {
        return playerState;
    }

    public void setPlayerState(int playerState) {
        this.playerState = playerState;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public void setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }
}

