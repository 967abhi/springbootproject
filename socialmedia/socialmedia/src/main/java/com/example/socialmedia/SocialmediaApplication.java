package com.example.socialmedia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

//@SpringBootApplication
public class SocialmediaApplication {

	public static void main(String[] args) {
//		SpringApplication.run(SocialmediaApplication.class, args);
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
		Scanner scanner = new Scanner(System.in);
		User user = (User) context.getBean("user");
		System.out.println("Please enter your username: ");
		String username = scanner.nextLine();

		user.setUserName(username);
		PostList postList = (PostList) context.getBean("postList");

	while(true){
		System.out.println("Choose from belwo : \n 1. Create a Post \n2.Sell All your post \n3.EXIT");
		int userSelect= scanner.nextInt();
		switch(userSelect){
			case 1:{
				Post post = (Post) context.getBean("post");
				scanner.nextLine();
				String message = scanner.nextLine();
				post.setMessage(message);
				postList.setPost(post);
				user.setPostList(postList);
				break;

			}
			case 2:
				postList.getPosts().forEach(item-> System.out.println(item.getMessage()));

				break;

				case 3:
				{
					context.close();
				}
		}
	}


	}

}
