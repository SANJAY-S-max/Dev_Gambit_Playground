package com.sanjay.quiz_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO — not a JPA entity, just wraps question fields for the user (no correct answer exposed)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionWrapper {

    private Integer id;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
}
