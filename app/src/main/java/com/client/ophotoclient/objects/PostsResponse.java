package com.client.ophotoclient.objects;

import java.util.List;

public class PostsResponse extends OphotoMessage {
    private List<String> posts;

    public PostsResponse(List<String> posts) {
        this.posts = posts;
    }

    public List<String> getPosts() {
        return posts;
    }

    public void setPosts(List<String> posts) {
        this.posts = posts;
    }
}
