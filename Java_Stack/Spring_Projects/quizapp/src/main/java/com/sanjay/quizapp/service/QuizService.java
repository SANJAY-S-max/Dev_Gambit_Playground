package com.sanjay.quizapp.service;

import com.sanjay.quizapp.dao.QuestionDao;
import com.sanjay.quizapp.dao.QuizDao;
import com.sanjay.quizapp.model.*;
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
    QuestionDao questionDao;

    // ─── CREATE QUIZ ───────────────────────────────────────────────────────────
    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        try {
            List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);
            if (questions.isEmpty()) {
                return new ResponseEntity<>("No questions found for category: " + category, HttpStatus.NOT_FOUND);
            }
            Quiz quiz = new Quiz();
            quiz.setTitle(title);
            quiz.setQuestions(questions);
            quizDao.save(quiz);
            return new ResponseEntity<>("Quiz created successfully with " + questions.size() + " questions.", HttpStatus.CREATED);
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
            List<Question> questionsFromDB = quizOpt.get().getQuestions();
            List<QuestionWrapper> questionsForUser = new ArrayList<>();
            for (Question q : questionsFromDB) {
                QuestionWrapper qw = new QuestionWrapper(
                        q.getId(), q.getQuestion(),
                        q.getOption1(), q.getOption2(),
                        q.getOption3(), q.getOption4()
                );
                questionsForUser.add(qw);
            }
            return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
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

            List<Question> questions = quizOpt.get().getQuestions();
            int correctCount = 0;
            int total = questions.size();

            for (UserAnswer userAnswer : userAnswers) {
                for (Question question : questions) {
                    if (question.getId().equals(userAnswer.getId())) {
                        if (question.getCorrectAnswer() != null && userAnswer.getGivenAnswer() != null
                                && question.getCorrectAnswer().trim().equalsIgnoreCase(userAnswer.getGivenAnswer().trim())) {
                            correctCount++;
                        }
                        break;
                    }
                }
            }

            double percentage = total > 0 ? Math.round((correctCount * 100.0 / total) * 10.0) / 10.0 : 0.0;
            QuizResult result = new QuizResult(correctCount, total, correctCount + "/" + total, percentage);
            return new ResponseEntity<>(result, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
