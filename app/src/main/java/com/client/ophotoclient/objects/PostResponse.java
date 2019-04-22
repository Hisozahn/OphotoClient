package com.client.ophotoclient.objects;

import java.util.List;

public class PostResponse extends OphotoMessage {
    private List<String> posts;

    public PostResponse(List<String> posts) {
        this.posts = posts;
    }

    public List<String> getPosts() {
        return posts;
    }

    public void setPosts(List<String> posts) {
        this.posts = posts;
    }
}
