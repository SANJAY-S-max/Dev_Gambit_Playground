package com.sanjay.quizapp.controller;

import com.sanjay.quizapp.model.QuestionWrapper;
import com.sanjay.quizapp.model.QuizResult;
import com.sanjay.quizapp.model.UserAnswer;
import com.sanjay.quizapp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    QuizService quizService;

    // ─── CREATE QUIZ ───────────────────────────────────────────────────────────
    // POST http://localhost:8080/quiz/create?category=Java&numQ=5&title=MyQuiz
    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestParam String category,
                                             @RequestParam int numQ,
                                             @RequestParam String title) {
        return quizService.createQuiz(category, numQ, title);
    }

    // ─── GET QUIZ QUESTIONS ────────────────────────────────────────────────────
    // GET http://localhost:8080/quiz/get/{id}
    @GetMapping("get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Integer id) {
        return quizService.getQuizQuestions(id);
    }

    // ─── SUBMIT ANSWERS & GET RESULT ──────────────────────────────────────────
    // POST http://localhost:8080/quiz/submit/{id}
    // Body: [{"id": 1, "givenAnswer": "Paris"}, {"id": 2, "givenAnswer": "4"}]
    @PostMapping("submit/{id}")
    public ResponseEntity<QuizResult> submitAnswers(@PathVariable Integer id,
                                                    @RequestBody List<UserAnswer> userAnswers) {
        return quizService.calculateResult(id, userAnswers);
    }
}
