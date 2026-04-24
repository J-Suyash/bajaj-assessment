package com.suyash.quiz_leaderboard.model;

public record LeaderboardEntry(
        String participant,
        long totalScore
) {
}
