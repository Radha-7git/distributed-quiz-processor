package com.srmquiz.quizleaderboard.service;

import com.srmquiz.quizleaderboard.model.LeaderboardEntry;
import com.srmquiz.quizleaderboard.model.PollResponse;
import com.srmquiz.quizleaderboard.model.QuizEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class QuizService {

    private static final String BASE_URL = "https://devapigw.vidalhealthtpa.com/srm-quiz-task";
    private static final String REG_NO = "RA2311003020407";
    private static final int TOTAL_POLLS = 10;
    private static final int DELAY_MS = 5000;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<LeaderboardEntry> fetchAndProcess() throws InterruptedException {
        Set<String> seen = new HashSet<>();
        Map<String, Integer> scores = new HashMap<>();

        for (int poll = 0; poll < TOTAL_POLLS; poll++) {
            String url = BASE_URL + "/quiz/messages?regNo=" + REG_NO + "&poll=" + poll;
            System.out.println("Polling " + poll + "...");

            PollResponse response = restTemplate.getForObject(url, PollResponse.class);

            if (response != null && response.getEvents() != null) {
                for (QuizEvent event : response.getEvents()) {
                    String key = event.getRoundId() + "|" + event.getParticipant();
                    if (seen.add(key)) {
                        scores.merge(event.getParticipant(), event.getScore(), Integer::sum);
                    }
                }
            }

            if (poll < TOTAL_POLLS - 1) {
                Thread.sleep(DELAY_MS);
            }
        }

        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        scores.forEach((participant, total) -> leaderboard.add(new LeaderboardEntry(participant, total)));
        leaderboard.sort((a, b) -> b.getTotalScore() - a.getTotalScore());

        return leaderboard;
    }

    public void submitLeaderboard(List<LeaderboardEntry> leaderboard) {
        String url = BASE_URL + "/quiz/submit";

        Map<String, Object> body = new HashMap<>();
        body.put("regNo", REG_NO);
        body.put("leaderboard", leaderboard);

        Map<?, ?> response = restTemplate.postForObject(url, body, Map.class);
        System.out.println("Submission response: " + response);
    }
}