package com.hsbc.boardie.model;

public class Post implements Comparable<Post> {
    private User user;
    private Message message;

    public Post(User user, Message message) {
        this.user = user;
        this.message = message;
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
