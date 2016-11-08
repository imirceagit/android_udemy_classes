package com.mient.instaslam.instaslam.model;

import android.net.Uri;

/**
 * Created by mircea.ionita on 11/8/2016.
 */

public class InstaImage {

    private Uri imgResourceUrl;

    public InstaImage(Uri imgResourceUrl) {
        this.imgResourceUrl = imgResourceUrl;
    }

    public Uri getImgResourceUrl() {
        return imgResourceUrl;
    }
}
