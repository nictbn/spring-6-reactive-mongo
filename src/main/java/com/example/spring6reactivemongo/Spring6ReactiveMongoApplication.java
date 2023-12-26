package com.example.spring6reactivemongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

@SpringBootApplication
@EnableReactiveMongoAuditing
public class Spring6ReactiveMongoApplication {

	public static void main(String[] args) {
		SpringApplication.run(Spring6ReactiveMongoApplication.class, args);
	}

}
