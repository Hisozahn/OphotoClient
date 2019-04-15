package com.client.ophotoclient.objects;

import io.realm.RealmObject;

import static com.client.ophotoclient.Definitions.debugEmptyString;

public class AuthUser extends RealmObject {
    private String name;
    private String token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AuthUser(String name, String token) {
        this.name = name;
        this.token = token;
    }

    public AuthUser() {
        this.name = debugEmptyString;
        this.token = debugEmptyString;
    }
}
