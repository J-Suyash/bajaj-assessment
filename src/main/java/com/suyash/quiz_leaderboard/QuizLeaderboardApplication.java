package com.suyash.quiz_leaderboard;

import com.suyash.quiz_leaderboard.client.QuizApiClient;
import com.suyash.quiz_leaderboard.model.LeaderboardResult;
import com.suyash.quiz_leaderboard.model.SubmitRequest;
import com.suyash.quiz_leaderboard.model.SubmitResponse;
import com.suyash.quiz_leaderboard.service.LeaderboardService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@SpringBootApplication
@ConfigurationPropertiesScan
public class QuizLeaderboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuizLeaderboardApplication.class, args);
    }

    @Bean
	@ConditionalOnProperty(name = "quiz.runner.enabled", havingValue = "true", matchIfMissing = true)
	CommandLineRunner run(
            QuizProperties quizProperties,
            LeaderboardService leaderboardService,
            QuizApiClient quizApiClient
    ) {
        return args -> {
            if (quizProperties.regNo() == null || quizProperties.regNo().isBlank()) {
                throw new IllegalArgumentException("REG_NO is required. Run with REG_NO=your_registration_number");
            }

            LeaderboardResult result = leaderboardService.buildLeaderboard(quizProperties.regNo());

            System.out.println();
            System.out.println("===== Final Result =====");
            System.out.println("Received events: " + result.receivedEvents());
            System.out.println("Unique events: " + result.uniqueEvents());
            System.out.println("Duplicate events ignored: " + result.duplicateEvents());
            System.out.println("Total score: " + result.totalScore());
            System.out.println("Leaderboard: " + result.leaderboard());

			if (!quizProperties.submit()) {
                System.out.println("Dry run completed. Submission skipped.");
                return;
            }
            SubmitRequest submitRequest = new SubmitRequest(
                    quizProperties.regNo(),
                    result.leaderboard()
            );
            try {
                SubmitResponse submitResponse = quizApiClient.submit(submitRequest);

                System.out.println();
                System.out.println("===== Submit Response =====");
                System.out.println(submitResponse);
            } catch (Exception exception) {
                System.out.println();
                System.out.println("===== Submit Failed =====");
                System.out.println(exception.getClass().getName());
                System.out.println(exception.getMessage());
                throw exception;
            }
        };
    }
}
