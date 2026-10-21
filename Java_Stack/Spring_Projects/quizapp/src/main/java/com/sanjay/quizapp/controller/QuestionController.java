package com.sanjay.quizapp.controller;

import com.sanjay.quizapp.model.Question;
import com.sanjay.quizapp.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    // ─── GET ALL QUESTIONS ─────────────────────────────────────────────────────
    @GetMapping("allQuestions")
    public ResponseEntity<List<Question>> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    // ─── GET QUESTIONS BY CATEGORY ─────────────────────────────────────────────
    @GetMapping("category/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category) {
        return questionService.getQuestionsByCategory(category);
    }

    // ─── ADD QUESTION ──────────────────────────────────────────────────────────
    @PostMapping("add")
    public ResponseEntity<Question> addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question);
    }

    // ─── UPDATE QUESTION ───────────────────────────────────────────────────────
    @PutMapping("update/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Integer id,
                                                   @RequestBody Question question) {
        return questionService.updateQuestion(id, question);
    }

    // ─── DELETE QUESTION ───────────────────────────────────────────────────────
    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Integer id) {
        return questionService.deleteQuestion(id);
    }
}
