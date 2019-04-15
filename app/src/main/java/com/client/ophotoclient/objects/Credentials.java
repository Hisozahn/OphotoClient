package com.client.ophotoclient.objects;

public class Credentials extends OphotoMessage {
    private String token;

    public Credentials(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
