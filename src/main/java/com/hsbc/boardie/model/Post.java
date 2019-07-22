package com.hsbc.boardie.model;

public class Post implements Comparable<Post> {

    private long id;
    private User user;
    private Message message;

    public Post(long id, User user, Message message) {
        this.id = id;
        this.user = user;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public int compareTo(Post post) {
        return this.getMessage().getTimestamp().compareTo(post.getMessage().getTimestamp());
    }
}
