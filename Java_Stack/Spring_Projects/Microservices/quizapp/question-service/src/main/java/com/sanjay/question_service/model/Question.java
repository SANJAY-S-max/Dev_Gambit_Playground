package com.sanjay.question_service.model;
//package com.sanjay.quizapp.model.DifficultyLevel;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false)
    private DifficultyLevel difficultyLevel;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(name = "option_1", nullable = false)
    private String option1;

    @Column(name = "option_2", nullable = false)
    private String option2;

    @Column(name = "option_3", nullable = false)
    private String option3;

    @Column(name = "option_4", nullable = false)
    private String option4;

    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;

    @Column(nullable = false, length = 100)
    private String categories;
}
