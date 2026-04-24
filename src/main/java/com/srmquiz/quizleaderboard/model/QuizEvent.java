package com.srmquiz.quizleaderboard.model;

import lombok.Data;

@Data
public class QuizEvent {
    private String roundId;
    private String participant;
    private int score;
}