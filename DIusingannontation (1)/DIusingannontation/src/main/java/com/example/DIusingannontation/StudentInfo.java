package com.example.DIusingannontation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("studentInfo")
public class StudentInfo {

    @Autowired
            @Qualifier("commerce")
    Student student;
    public void showDetails() {
        System.out.println(student.college());

    }
}
