package com.example.socialmedia;

public class Simple implements Post {
    String message;

    @Override
    public void setMessage(String message) {
     this.message = message;
    }
    @Override
    public String getMessage() {
        return this.message;
    }
}
