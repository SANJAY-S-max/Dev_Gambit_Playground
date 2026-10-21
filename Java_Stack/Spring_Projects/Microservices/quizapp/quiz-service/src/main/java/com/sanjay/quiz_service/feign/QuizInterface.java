package com.sanjay.quiz_service.feign;

import com.sanjay.quiz_service.model.QuestionWrapper;
import com.sanjay.quiz_service.model.UserAnswer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "QUESTION-SERVICE")
public interface QuizInterface {

    @GetMapping("question/generate")
    ResponseEntity<List<Integer>> getQuestionForQuiz(@RequestParam("categoryName") String categoryName,
                                                    @RequestParam("numQuestions") Integer numQuestions);

    @PostMapping("question/getQuestions")
    ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionIds);

    @PostMapping("question/getScore")
    ResponseEntity<Integer> getScore(@RequestBody List<UserAnswer> responses);
}
