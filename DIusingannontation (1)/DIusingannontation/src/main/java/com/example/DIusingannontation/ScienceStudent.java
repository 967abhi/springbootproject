package com.example.DIusingannontation;

import org.springframework.stereotype.Component;

@Component("science")
public class ScienceStudent implements Student {
    @Override
    public String college() {
        return "Science Student";
    }
}
