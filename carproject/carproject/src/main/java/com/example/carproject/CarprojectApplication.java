package com.example.carproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

//@SpringBootApplication
public class CarprojectApplication {

	public static void main(String[] args) {
//		SpringApplication.run(CarprojectApplication.class, args);
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
//		Car car=(Car) context.getBean("familycar");
		Scanner scanner = new Scanner(System.in);
		System.out.println("Choose the car you wanna buy :\n 1. family car \n2. sports car \n3.cybertruck car");

		int userselect=scanner.nextInt();
		String beanId="";
//		car.speed();
		switch (userselect){
		case 1:
			beanId="family";
			break;
			case 2:
				beanId="sports";
				break;
			case 3:
				beanId="cybertruck";
				break;
		}
		Car car = (Car) context.getBean(beanId);
		car.showDetails();



	}

}
