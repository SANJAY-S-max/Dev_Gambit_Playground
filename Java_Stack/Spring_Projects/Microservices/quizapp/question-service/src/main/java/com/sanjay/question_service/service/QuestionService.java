package com.sanjay.question_service.service;

import com.sanjay.question_service.dao.QuestionDao;
import com.sanjay.question_service.model.Question;
import com.sanjay.question_service.model.QuestionWrapper;
import com.sanjay.question_service.model.UserAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    // ─── GET ALL QUESTIONS ─────────────────────────────────────────────────────
    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            List<Question> questions = questionDao.findAll();
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ─── GET QUESTIONS BY CATEGORY ─────────────────────────────────────────────
    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            List<Question> questions = questionDao.findByCategories(category);
            if (questions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ─── ADD QUESTION ──────────────────────────────────────────────────────────
    public ResponseEntity<Question> addQuestion(Question question) {
        try {
            Question savedQuestion = questionDao.save(question);
            return new ResponseEntity<>(savedQuestion, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ─── UPDATE QUESTION ───────────────────────────────────────────────────────
    public ResponseEntity<Question> updateQuestion(Integer id, Question updatedQuestion) {
        try {
            Optional<Question> existingQuestion = questionDao.findById(id);
            if (existingQuestion.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Question question = existingQuestion.get();
            question.setQuestion(updatedQuestion.getQuestion());
            question.setOption1(updatedQuestion.getOption1());
            question.setOption2(updatedQuestion.getOption2());
            question.setOption3(updatedQuestion.getOption3());
            question.setOption4(updatedQuestion.getOption4());
            question.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
            question.setDifficultyLevel(updatedQuestion.getDifficultyLevel());
            question.setCategories(updatedQuestion.getCategories());
            Question savedQuestion = questionDao.save(question);
            return new ResponseEntity<>(savedQuestion, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ─── DELETE QUESTION ───────────────────────────────────────────────────────
    public ResponseEntity<String> deleteQuestion(Integer id) {
        try {
            if (!questionDao.existsById(id)) {
                return new ResponseEntity<>("Question with id " + id + " not found.", HttpStatus.NOT_FOUND);
            }
            questionDao.deleteById(id);
            return new ResponseEntity<>("Question deleted successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete question: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Integer>> getQuestionForQuiz(String categoryName, Integer numQuestions) {
        try {
            List<Integer> questions = questionDao.findRandomQuestionsByCategory(categoryName, numQuestions != null ? numQuestions : 5);
            return new ResponseEntity<>(questions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionFromId(List<Integer> questionIds) {
        try {
            List<QuestionWrapper> wrappers = new ArrayList<>();
            if (questionIds != null) {
                for (Integer id : questionIds) {
                    Optional<Question> qOpt = questionDao.findById(id);
                    if (qOpt.isPresent()) {
                        Question question = qOpt.get();
                        QuestionWrapper wrapper = new QuestionWrapper();
                        wrapper.setId(question.getId());
                        wrapper.setQuestion(question.getQuestion());
                        wrapper.setOption1(question.getOption1());
                        wrapper.setOption2(question.getOption2());
                        wrapper.setOption3(question.getOption3());
                        wrapper.setOption4(question.getOption4());
                        wrappers.add(wrapper);
                    }
                }
            }
            return new ResponseEntity<>(wrappers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Integer> getScore(List<UserAnswer> userAnswers) {
        try {
            int score = 0;
            if (userAnswers != null) {
                for (UserAnswer answer : userAnswers) {
                    if (answer.getId() != null && answer.getGivenAnswer() != null) {
                        Optional<Question> qOpt = questionDao.findById(answer.getId());
                        if (qOpt.isPresent()) {
                            Question q = qOpt.get();
                            if (q.getCorrectAnswer() != null &&
                                    q.getCorrectAnswer().trim().equalsIgnoreCase(answer.getGivenAnswer().trim())) {
                                score++;
                            }
                        }
                    }
                }
            }
            return new ResponseEntity<>(score, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
