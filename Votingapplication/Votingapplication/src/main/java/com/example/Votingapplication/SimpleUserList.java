package com.example.Votingapplication;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("simpleuserlist")
public class SimpleUserList implements UserList {
    List<User> listofusers;
    public SimpleUserList() {
        this.listofusers = new ArrayList<User>();

    }

    @Override
    public void addUser(User user) {

   listofusers.add(user);
    }

    @Override
    public List<User> getUsers() {
        return this.listofusers;
    }
}
