package com.client.ophotoclient.objects;

import android.graphics.Bitmap;

import java.util.List;

public class UserResponse extends OphotoMessage {
    private String bio;
    private List<String> follows;
    private Bitmap image;

    public UserResponse(String bio, List<String> follows, Bitmap image) {
        this.bio = bio;
        this.follows = follows;
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getFollows() {
        return follows;
    }

    public void setFollows(List<String> follows) {
        this.follows = follows;
    }
}
