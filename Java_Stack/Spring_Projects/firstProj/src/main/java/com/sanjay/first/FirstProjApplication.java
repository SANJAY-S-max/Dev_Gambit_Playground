package com.sanjay.first;

import org.springframework.boot.SpringApplication; 
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class FirstProjApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(FirstProjApplication.class, args);
		Alien obj = context.getBean(Alien.class);
		obj.code();
	}
}