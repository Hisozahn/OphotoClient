package com.client.ophotoclient.objects;

import android.graphics.Bitmap;

public class UserRow {
    private String name;
    private Bitmap image;

    public UserRow(String name, Bitmap image) {
        this.image = image;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
