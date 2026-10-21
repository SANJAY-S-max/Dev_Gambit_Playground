package com.sanjay.question_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO — the user's submitted answer for a single question
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswer {
    private Integer id;           // question ID
    private String givenAnswer;   // user's chosen answer
}
