package io.codecrafts.McqPilot.service;

import io.codecrafts.McqPilot.model.Question;
import io.codecrafts.McqPilot.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionServiceImpl implements QuestionService {

	@Autowired
	private QuestionRepository questionRepository;

	@Override
	public List<Question> getAll() {
		List<Question> questions = new ArrayList<>();
		questionRepository.findAll().forEach(questions::add);
		return questions;
	}

	@Override
	public void saveQuestion(Question question) {
		questionRepository.save(question);
	}

    public Question findQuestion(UUID id) {
		Question question = questionRepository.findOne(id);
	    return question;
    }

	@Override
	public void deleteQuestion(UUID id) {
		questionRepository.delete(id);
	}
}
