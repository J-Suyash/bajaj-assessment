package com.suyash.quiz_leaderboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class QuizLeaderboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizLeaderboardApplication.class, args);
	}

}
