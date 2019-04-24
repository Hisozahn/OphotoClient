package com.client.ophotoclient.objects;

import android.graphics.Bitmap;

public class Post extends OphotoMessage{
    private String description;
    private Bitmap image;
    private String userName;
    private Bitmap userImage;
    private String id;

    public String getId() {
        return id;
    }

    public Bitmap getUserImage() {
        return userImage;
    }

    public void setUserImage(Bitmap userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Post(String id, String description, Bitmap image, String user) {
        this.id = id;
        this.description = description;
        this.image = image;
        this.userName = user;
    }
}
