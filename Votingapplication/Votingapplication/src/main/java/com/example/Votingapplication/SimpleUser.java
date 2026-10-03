package com.example.Votingapplication;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("simpleuser")
@Scope("prototype")
public class SimpleUser implements User {


    private String userName;
    private PoliticalParty politicalPartyName;
    @Override
    public void setUsername(String username) {
        this.userName = username;

    }
    @Override
    public String getUsername() {
        return this.userName;
    }
    @Override
    public void setPoliticalParty(PoliticalParty politicalParty){
      this.politicalPartyName=politicalParty;
    }
    @Override
    public PoliticalParty getPoliticalParty( ) {
        return this.politicalPartyName;
    }

}
