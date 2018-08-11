package io.codecrafts.McqPilot.service;

import io.codecrafts.McqPilot.model.Question;

import java.util.List;
import java.util.UUID;

public interface QuestionService {
	List<Question> getAll();
	Question findQuestion(UUID id);
	void saveQuestion(Question question);
	void deleteQuestion(UUID id);
}
