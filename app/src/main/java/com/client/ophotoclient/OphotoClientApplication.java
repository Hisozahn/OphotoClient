package com.client.ophotoclient;

import android.app.Application;

import io.realm.Realm;

public class OphotoClientApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
