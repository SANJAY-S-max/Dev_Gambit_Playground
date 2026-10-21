package com.sanjay.quiz_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDto {
    private String categoryName;
    private Integer numQuestions;
    private String title;

    // Backwards-compatible getter & setter for typo numQuestons
    public Integer getNumQuestons() {
        return numQuestions;
    }

    public void setNumQuestons(Integer numQuestons) {
        this.numQuestions = numQuestons;
    }
}
