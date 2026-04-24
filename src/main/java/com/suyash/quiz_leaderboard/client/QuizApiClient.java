package com.suyash.quiz_leaderboard.client;

import com.suyash.quiz_leaderboard.QuizProperties;
import com.suyash.quiz_leaderboard.model.QuizMessagesResponse;
import com.suyash.quiz_leaderboard.model.SubmitRequest;
import com.suyash.quiz_leaderboard.model.SubmitResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class QuizApiClient {

    private final RestTemplate restTemplate;
    private final QuizProperties quizProperties;

    public QuizApiClient(QuizProperties quizProperties) {
        this.restTemplate = new RestTemplate();
        this.quizProperties = quizProperties;
    }

    public QuizMessagesResponse getMessages(String regNo, int poll) {
        URI uri = UriComponentsBuilder
                .fromUriString(quizProperties.baseUrl())
                .path("/quiz/messages")
                .queryParam("regNo", regNo)
                .queryParam("poll", poll)
                .build()
                .toUri();

        return restTemplate.getForObject(uri, QuizMessagesResponse.class);
    }

    public SubmitResponse submit(SubmitRequest request) {
        URI uri = UriComponentsBuilder
                .fromUriString(quizProperties.baseUrl())
                .path("/quiz/submit")
                .build()
                .toUri();

        return restTemplate.postForObject(uri, request, SubmitResponse.class);
    }
}
