package com.example.socialmedia;

import java.util.ArrayList;
import java.util.List;

public class SimplePostList implements PostList {
    List<Post> listOfPosts=null;
    public SimplePostList(){
        this.listOfPosts=new ArrayList<>();
    }
    @Override
    public void setPost(Post post){
//        listOfPosts=new ArrayList<>();
        listOfPosts.add(post);
    }

    @Override
    public List<Post> getPosts(){
        return this.listOfPosts;
    }


}
