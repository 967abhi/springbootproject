package com.example.Votingapplication;

import org.springframework.stereotype.Component;

@Component("simpleauthority")
public class SimpleAuthoritycounter implements AuthorityCounter {
    private UserList userlist;
    @Override
    public void setUserlist(UserList userList) {
        this.userlist = userList;

    }
    @Override
    public UserList getUserlist() {
        return this.userlist;
    }
}
