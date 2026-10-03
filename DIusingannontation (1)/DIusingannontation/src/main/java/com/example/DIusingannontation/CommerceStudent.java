package com.example.DIusingannontation;

import org.springframework.stereotype.Component;

@Component("commerce")
public class CommerceStudent implements Student {
    @Override
    public String college(){
        return "Commerce student from gkp";
    }
}
