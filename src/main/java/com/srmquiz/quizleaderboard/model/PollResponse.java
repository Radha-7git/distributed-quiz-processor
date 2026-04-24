package com.srmquiz.quizleaderboard.model;

import lombok.Data;
import java.util.List;

@Data
public class PollResponse {
    private String regNo;
    private String setId;
    private int pollIndex;
    private List<QuizEvent> events;
}