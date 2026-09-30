package com.example.LaptopDealer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLOutput;
import java.util.Scanner;

//@SpringBootApplication
public class LaptopDealerApplication {

	public static void main(String[] args) {
//		SpringApplication.run(LaptopDealerApplication.class, args);
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//		Brand brand = context.getBean(Brand.class);
//		Processor processor = context.getBean(Processor.class);
//		brand.ShowDetails();
		Scanner scanner=new Scanner(System.in);
		System.out.println("Choose the laptop \n1. Dell \n2.Mackbook\n3.Microsoft");
		int userBrandSelect=scanner.nextInt();
		System.out.println("choose the process ypu want  :\n1.i3\n2.i5\n3.i7");
		int userProcessSelect=scanner.nextInt();
		String beandId="";
		switch(userBrandSelect){
			case 1:{
				switch(userProcessSelect){
					case 1:{
						beandId="dellwithi3";
						break;
					}
					case 2:{
						beandId="dellwithi5";
						break;
					}
					case 3:{
						beandId="dellwithi7";
						break;
					}
				}
				break;
			}
			case 2:{
				switch(userProcessSelect){
					case 1:{
						beandId="mackbookwithi3";
						break;
					}
					case 2:{
						beandId="mackbookwithi5";
						break;
					}
					case 3:{
						beandId="mackbookwithi7";
						break;
					}
				}
				break;
			}
			case 3:{
				switch(userProcessSelect){
					case 1:{
						beandId="microsoftlaptopwithi3";
						break;
					}
					case 2:{
						beandId="microsoftlaptopwithi5";
						break;
					}
					case 3:{
						beandId="microsoftlaptopwithi7";
						break;
					}
				}
				break;
			}

		}
      Brand brand=(Brand) context.getBean(beandId);
		brand.ShowDetails();
	}

}
