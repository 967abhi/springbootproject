package com.example.socialmedia;

public class SimpleUser implements User {
    String username;
    PostList postList;
    public void init(){
        System.out.println("Db connected successfully");
    }
    public void destroy(){
        System.out.println("Db disconnected successfully");
    }


    @Override
    public void setUserName(String username)
        {
        this.username = username;
        }

        @Override
    public String getUserName(){
        return this.username;
        }
        @Override
        public void setPostList(PostList postList){
        this.postList = postList;
        }

        @Override
    public PostList getPostList(){
        return this.postList;
        }
}
