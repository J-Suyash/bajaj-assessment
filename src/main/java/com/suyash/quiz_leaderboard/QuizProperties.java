package com.suyash.quiz_leaderboard;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "quiz")
public record QuizProperties(
        String baseUrl,
        String regNo,
        boolean submit
) {
}
