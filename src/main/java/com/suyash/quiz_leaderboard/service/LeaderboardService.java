package com.suyash.quiz_leaderboard.service;

import com.suyash.quiz_leaderboard.client.QuizApiClient;
import com.suyash.quiz_leaderboard.model.LeaderboardEntry;
import com.suyash.quiz_leaderboard.model.LeaderboardResult;
import com.suyash.quiz_leaderboard.model.QuizEvent;
import com.suyash.quiz_leaderboard.model.QuizMessagesResponse;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class LeaderboardService {

    private static final int TOTAL_POLLS = 10;
    private static final long POLL_DELAY_MS = Duration.ofSeconds(5).toMillis();

    private final QuizApiClient quizApiClient;

    public LeaderboardService(QuizApiClient quizApiClient) {
        this.quizApiClient = quizApiClient;
    }

    public LeaderboardResult buildLeaderboard(String regNo) throws InterruptedException {
        List<QuizEvent> allEvents = new ArrayList<>();

        for (int poll = 0; poll < TOTAL_POLLS; poll++) {
            QuizMessagesResponse response = quizApiClient.getMessages(regNo, poll);

            if (response == null || response.events() == null) {
                throw new IllegalStateException("Invalid response received for poll " + poll);
            }

            System.out.println("Poll " + poll + " completed. Events received: " + response.events().size());

            for (QuizEvent event : response.events()) {
                validateEvent(event, poll);
            }

            allEvents.addAll(response.events());

            if (poll < TOTAL_POLLS - 1) {
                Thread.sleep(POLL_DELAY_MS);
            }
        }

        return buildLeaderboardFromEvents(allEvents);
    }

    LeaderboardResult buildLeaderboardFromEvents(List<QuizEvent> events) {
        Set<String> processedEvents = new HashSet<>();
        Map<String, Long> participantScores = new HashMap<>();

        int receivedEvents = 0;
        int duplicateEvents = 0;

        for (QuizEvent event : events) {
            validateEvent(event, -1);
            receivedEvents++;

            String eventKey = event.roundId() + "|" + event.participant();

            if (processedEvents.add(eventKey)) {
                participantScores.merge(event.participant(), (long) event.score(), Long::sum);
            } else {
                duplicateEvents++;
            }
        }

        List<LeaderboardEntry> leaderboard = participantScores.entrySet()
                .stream()
                .map(entry -> new LeaderboardEntry(entry.getKey(), entry.getValue()))
                .sorted(
                        Comparator.comparingLong(LeaderboardEntry::totalScore)
                                .reversed()
                                .thenComparing(LeaderboardEntry::participant)
                )
                .toList();

        long totalScore = leaderboard.stream()
                .mapToLong(LeaderboardEntry::totalScore)
                .sum();

        return new LeaderboardResult(
                leaderboard,
                totalScore,
                receivedEvents,
                processedEvents.size(),
                duplicateEvents
        );
    }

    private void validateEvent(QuizEvent event, int poll) {
        if (event == null) {
            throw new IllegalStateException("Null event found in poll " + poll);
        }

        if (event.roundId() == null || event.roundId().isBlank()) {
            throw new IllegalStateException("Missing roundId in poll " + poll);
        }

        if (event.participant() == null || event.participant().isBlank()) {
            throw new IllegalStateException("Missing participant in poll " + poll);
        }
    }
}