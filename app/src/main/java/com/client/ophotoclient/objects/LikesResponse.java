package com.client.ophotoclient.objects;

import java.util.List;

public class LikesResponse extends OphotoMessage {
    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    private List<String> likes;
}
