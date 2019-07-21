package com.hsbc.boardie.model;

import java.util.HashSet;
import java.util.Set;

public class User {

    private String login;
    private Set<String> following = new HashSet<>();

    public User(String login) {
        this.login = login;
    }

    public Set<String> getFollowing() {
        return following;
    }

    public void addToFollowing(String follow) {
        this.following.add(follow);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString(){
        return login;
    }
}
