package com.sanjay.question_service.controller;

import com.sanjay.question_service.model.Question;
import com.sanjay.question_service.model.QuestionWrapper;
import com.sanjay.question_service.model.UserAnswer;
import com.sanjay.question_service.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"question", "questions"})
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    Environment environment;

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

    @GetMapping("generate")
    public ResponseEntity<List<Integer>> getQuestionForQuiz(
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "catagoryName", required = false) String catagoryName,
            @RequestParam(value = "numQuestions", defaultValue = "5") Integer numQuestions) {
        String category = categoryName != null ? categoryName : catagoryName;
        return questionService.getQuestionForQuiz(category, numQuestions);
    }

    @PostMapping("getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionIds) {
        return questionService.getQuestionFromId(questionIds);
    }

    @PostMapping("getScore")
    public ResponseEntity<Integer> getScore(@RequestBody List<UserAnswer> responses) {
        return questionService.getScore(responses);
    }

}
