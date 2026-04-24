package com.suyash.quiz_leaderboard.model;

import java.util.List;

public record QuizMessagesResponse(
        String regNo,
        String setId,
        int pollIndex,
        List<QuizEvent> events
) {
}
