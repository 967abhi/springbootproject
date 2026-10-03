package com.example.Votingapplication;

import org.springframework.stereotype.Component;

@Component("independent")
public class IndependentParty implements PoliticalParty{
    @Override
    public String PartyName() {
        return "Independent Party";
    }
}
