package com.iglo.chatbothelpdesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAspectJAutoProxy
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class IgloApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgloApplication.class, args);
	}

}
