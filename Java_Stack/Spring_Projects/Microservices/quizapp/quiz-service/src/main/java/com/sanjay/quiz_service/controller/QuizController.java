package com.sanjay.quiz_service.controller;

import com.sanjay.quiz_service.model.QuestionWrapper;
import com.sanjay.quiz_service.model.QuizDto;
import com.sanjay.quiz_service.model.QuizResult;
import com.sanjay.quiz_service.model.UserAnswer;
import com.sanjay.quiz_service.service.QuizService;
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
    // Supports JSON body (QuizDto) or query params (?category=Java&numQ=5&title=MyQuiz)
    @PostMapping("create")
    public ResponseEntity<String> createQuiz(
            @RequestBody(required = false) QuizDto quizDto,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer numQ,
            @RequestParam(required = false) String title) {
        if (quizDto != null && quizDto.getCategoryName() != null) {
            return quizService.createQuiz(quizDto.getCategoryName(), quizDto.getNumQuestions(), quizDto.getTitle());
        }
        return quizService.createQuiz(category, numQ != null ? numQ : 5, title);
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
