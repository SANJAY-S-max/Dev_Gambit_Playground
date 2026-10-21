package com.sanjay.quiz_service.service;

import com.sanjay.quiz_service.dao.QuizDao;
import com.sanjay.quiz_service.feign.QuizInterface;
import com.sanjay.quiz_service.model.QuestionWrapper;
import com.sanjay.quiz_service.model.Quiz;
import com.sanjay.quiz_service.model.QuizResult;
import com.sanjay.quiz_service.model.UserAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    // ─── CREATE QUIZ ───────────────────────────────────────────────────────────
    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        try {
            ResponseEntity<List<Integer>> response = quizInterface.getQuestionForQuiz(category, numQ);
            List<Integer> questions = response != null ? response.getBody() : null;
            if (questions == null || questions.isEmpty()) {
                return new ResponseEntity<>("No questions found for category: " + category, HttpStatus.NOT_FOUND);
            }
            Quiz quiz = new Quiz();
            quiz.setTitle(title);
            quiz.setQuestionIds(questions);
            Quiz savedQuiz = quizDao.save(quiz);
            return new ResponseEntity<>("Quiz created successfully with id " + savedQuiz.getId(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create quiz: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ─── GET QUIZ QUESTIONS (no correct answer exposed) ────────────────────────
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        try {
            Optional<Quiz> quizOpt = quizDao.findById(id);
            if (quizOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Quiz quiz = quizOpt.get();
            List<Integer> questionIds = quiz.getQuestionIds();
            if (questionIds == null || questionIds.isEmpty()) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
            }
            ResponseEntity<List<QuestionWrapper>> questionsResp = quizInterface.getQuestionsFromId(questionIds);
            List<QuestionWrapper> wrappers = questionsResp != null ? questionsResp.getBody() : new ArrayList<>();
            return new ResponseEntity<>(wrappers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ─── SUBMIT ANSWERS & CALCULATE RESULT ────────────────────────────────────
    public ResponseEntity<QuizResult> calculateResult(Integer id, List<UserAnswer> userAnswers) {
        try {
            Optional<Quiz> quizOpt = quizDao.findById(id);
            if (quizOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Quiz quiz = quizOpt.get();
            int total = quiz.getQuestionIds() != null ? quiz.getQuestionIds().size() : (userAnswers != null ? userAnswers.size() : 0);

            ResponseEntity<Integer> scoreResp = quizInterface.getScore(userAnswers);
            int correctCount = (scoreResp != null && scoreResp.getBody() != null) ? scoreResp.getBody() : 0;

            double percentage = total > 0 ? Math.round((correctCount * 100.0 / total) * 10.0) / 10.0 : 0.0;
            QuizResult result = new QuizResult(correctCount, total, correctCount + "/" + total, percentage);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
