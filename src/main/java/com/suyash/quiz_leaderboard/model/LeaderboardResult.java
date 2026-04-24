package com.suyash.quiz_leaderboard.model;

import java.util.List;

public record LeaderboardResult(
        List<LeaderboardEntry> leaderboard,
        long totalScore,
        int receivedEvents,
        int uniqueEvents,
        int duplicateEvents
) {
}
