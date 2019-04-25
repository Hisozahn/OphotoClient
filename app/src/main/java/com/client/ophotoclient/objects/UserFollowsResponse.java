package com.client.ophotoclient.objects;

public class UserFollowsResponse extends OphotoMessage {
    private String follows;

    public String getFollows() {
        return follows;
    }

    public void setFollows(String follows) {
        this.follows = follows;
    }
}
