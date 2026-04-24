package com.suyash.quiz_leaderboard.model;

public record SubmitResponse(
        boolean isCorrect,
        boolean isIdempotent,
        long submittedTotal,
        long expectedTotal,
        String message
) {
}
