package com.client.ophotoclient.objects;

import android.graphics.Bitmap;

public class Post extends OphotoMessage{
    private String description;
    private Bitmap image;

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

    public Post(String description, Bitmap image) {
        this.description = description;
        this.image = image;
    }
}
