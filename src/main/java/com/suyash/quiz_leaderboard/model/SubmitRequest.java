package com.suyash.quiz_leaderboard.model;

import java.util.List;

public record SubmitRequest(
        String regNo,
        List<LeaderboardEntry> leaderboard
) {
}
