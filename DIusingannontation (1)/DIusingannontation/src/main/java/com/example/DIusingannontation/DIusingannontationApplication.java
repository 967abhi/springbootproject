package com.example.DIusingannontation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

//@SpringB/ootApplication
public class DIusingannontationApplication {

	public static void main(String[] args) {
//		SpringApplication.run(DIusingannontationApplication.class, args);
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.example.DIusingannontation");
		System.out.println("Choose the field\n1.science \n2.commerce");
		Scanner scanner = new Scanner(System.in);
		int choice = scanner.nextInt();
		String selectedField="";
		switch (choice) {
			case 1:{
				selectedField="science";
				break;

			}
			case 2:{
				selectedField="commerce";
				break;

			}
		}

		Student student=(Student)context.getBean(selectedField);
		StudentInfo studentInfo=context.getBean(StudentInfo.class);
		studentInfo.student=student;
		studentInfo.showDetails();


	}

}
