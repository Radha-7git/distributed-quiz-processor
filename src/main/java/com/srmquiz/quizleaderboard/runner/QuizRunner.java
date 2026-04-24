package com.srmquiz.quizleaderboard.runner;

import com.srmquiz.quizleaderboard.model.LeaderboardEntry;
import com.srmquiz.quizleaderboard.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuizRunner implements CommandLineRunner {

    private final QuizService quizService;

    @Override
    public void run(String... args) throws Exception {
        List<LeaderboardEntry> leaderboard = quizService.fetchAndProcess();

        System.out.println("\n=== LEADERBOARD ===");
        int total = 0;
        for (LeaderboardEntry entry : leaderboard) {
            System.out.println(entry.getParticipant() + " → " + entry.getTotalScore());
            total += entry.getTotalScore();
        }
        System.out.println("Total Score: " + total);
        System.out.println("===================\n");

        quizService.submitLeaderboard(leaderboard);
    }
}