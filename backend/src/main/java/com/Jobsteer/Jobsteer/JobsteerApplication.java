package com.Jobsteer.Jobsteer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobsteerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobsteerApplication.class, args);
	}
}