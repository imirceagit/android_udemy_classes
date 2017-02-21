package com.mient.mimusicplayer.model;

/**
 * Created by mircea.ionita on 2/21/2017.
 */

public interface MusicPlayer {

    public void play();

    public void pause();

    public void prev();

    public void next();

    public void stop();

    public void seek(int progress);

    public void shuffle();

    public void repeat();

    public void close();
}
