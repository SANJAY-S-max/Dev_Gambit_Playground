package com.sanjay.quizapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO — the final result returned to the client after answer submission
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResult {
    private int correctAnswers;
    private int totalQuestions;
    private String score;        // e.g. "3/5"
    private double percentage;   // e.g. 60.0
}
