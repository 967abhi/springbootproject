package com.example.Votingapplication;

import org.springframework.stereotype.Component;

@Component("democracy")
public class DemocracyParty implements PoliticalParty {
    @Override
    public String PartyName() {
        return "Democracy Party";
    }
}
