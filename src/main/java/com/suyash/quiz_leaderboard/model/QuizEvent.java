package com.suyash.quiz_leaderboard.model;

public record QuizEvent(
        String roundId,
        String participant,
        int score
) {
}
