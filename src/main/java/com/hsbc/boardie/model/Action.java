package com.hsbc.boardie.model;

public class Action {

    private String login;
    private String content;

    public Action(String login, String content) {
        this.login = login;
        this.content = content;
    }

    public String getLogin() {
        return login;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString(){
        return login + " -> " + content;
    }

}
