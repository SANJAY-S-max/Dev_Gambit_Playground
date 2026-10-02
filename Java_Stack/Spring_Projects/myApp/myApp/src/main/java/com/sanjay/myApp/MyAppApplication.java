package com.sanjay.myApp;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MyAppApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(MyAppApplication.class, args);
		Dev dev;
        dev = context.getBean(Dev.class);
		dev.build();
    }
}
