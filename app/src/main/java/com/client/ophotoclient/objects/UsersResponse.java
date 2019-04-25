package com.client.ophotoclient.objects;

import java.util.List;

public class UsersResponse extends OphotoMessage {
    private List<String> users;

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
