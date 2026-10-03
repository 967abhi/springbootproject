package com.example.Votingapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Scanner;

//@SpringBootApplication
public class VotingapplicationApplication {

	public static void main(String[] args) {
//		SpringApplication.run(VotingapplicationApplication.class, args);
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("com.example.Votingapplication");
	     AuthorityCounter authorityCounter= (AuthorityCounter)context.getBean("simpleauthority");
	while(true){
		System.out.println("Welcome to Votingapplication");
		Scanner scanner = new Scanner(System.in);
		System.out.println("Choose your choice\n1.I wanna vote \n2.See all votes(Admin)");
		int userInput = scanner.nextInt();
		String choice = "";
		switch (userInput) {
			case 1:{
				System.out.println("Enter your username");
				String username = scanner.next();
				User user=(User)context.getBean("simpleuser");
				user.setUsername(username);
				System.out.println("Choose the party  you want to vote \n1.Democractic \n2.Republic\n3.Independent");
				int userPartySelect = scanner.nextInt();
				switch (userPartySelect) {
					case 1:{
						choice = "democracy";
						break;

					}
					case 2:{
						choice = "republic";
						break;
					}
					case 3:{
						choice = "independent";
						break;
					}
				}
				PoliticalParty politicalParty=(PoliticalParty) context.getBean(choice);
				user.setPoliticalParty(politicalParty);
				UserList userList =(UserList)context.getBean("simpleuserlist");
				userList.addUser(user);
				authorityCounter.setUserlist(userList);
				System.out.println("Thank you for your voting");
				break;

			}
			case 2:{
				authorityCounter.getUserlist().getUsers().forEach(item->System.out.println(item.getUsername()+" is voted for " + item.getPoliticalParty().PartyName()));
				break;

			}
		}
	}
	}

}
