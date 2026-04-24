package com.suyash.quiz_leaderboard.model;

public record SubmitResponse(
        Boolean isCorrect,
        Boolean isIdempotent,
        Long submittedTotal,
        Long expectedTotal,
        String message
) {
}
