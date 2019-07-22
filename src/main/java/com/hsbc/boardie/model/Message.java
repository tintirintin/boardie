package com.hsbc.boardie.model;

import java.util.Date;

public class Message {

    private String message;
    private Date timestamp;

    public Message(String message, Date timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString(){
        return message + " @" + timestamp;
    }
}
