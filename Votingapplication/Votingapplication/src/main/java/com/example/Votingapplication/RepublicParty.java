package com.example.Votingapplication;

import org.springframework.stereotype.Component;

@Component("republic")
public class RepublicParty implements PoliticalParty {
    @Override
    public String PartyName() {
        return "Republic party";
    }
}
