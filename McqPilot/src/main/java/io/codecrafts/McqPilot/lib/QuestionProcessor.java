package io.codecrafts.McqPilot.lib;

import io.codecrafts.McqPilot.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionProcessor {
    private int numSessionQuestions;
    private Question currentQuestion;
    private int indexQuestion;
    List<Question> totalAvailableQuestions;
    List<Question> sessionQuestions;

    public boolean isRandomizeQuestions() {
        return randomizeQuestions;
    }

    public void setRandomizeQuestions(boolean randomizeQuestions) {
        this.randomizeQuestions = randomizeQuestions;
    }

    private boolean randomizeQuestions;

    public QuestionProcessor(int numSessionQuestions, List<Question> totalAvailableQuestions, boolean randomizeQuestions) {
        this.numSessionQuestions = numSessionQuestions;
        this.totalAvailableQuestions = totalAvailableQuestions;
        this.randomizeQuestions = randomizeQuestions;
    }

    public void init() {
        sessionQuestions = new ArrayList<>();

        List<Question> temp = new ArrayList<>();
        temp.addAll(totalAvailableQuestions);

        if (randomizeQuestions) {
            Collections.shuffle(temp);
        }

        int count = 0;
        for (Question question: temp) {
            sessionQuestions.add(question);
            count++;

            if (count >= numSessionQuestions) {
                break;
            }
        }
    }

    private int getIndexQuestion() {
        return indexQuestion;
    }

    private void setIndexQuestion(int indexQuestion) {
        this.indexQuestion = indexQuestion;
    }

    private Question getCurrentQuestion() {
        return currentQuestion;
    }

    private void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public List<Question> getTotalAvailableQuestions() {
        return totalAvailableQuestions;
    }

    public void setTotalAvailableQuestions(List<Question> totalAvailableQuestions) {
        this.totalAvailableQuestions = totalAvailableQuestions;
    }

    public List<Question> getSessionQuestions() {
        return sessionQuestions;
    }

    private void setSessionQuestions(List<Question> sessionQuestions) {
        this.sessionQuestions = sessionQuestions;
    }

    public int getNumSessionQuestions() {
        return numSessionQuestions;
    }

    public void setNumSessionQuestions(int numSessionQuestions) {
        this.numSessionQuestions = numSessionQuestions;
    }

    public int getTotalQuestions() {
        return totalAvailableQuestions.size();
    }

    public boolean hasNextQuestion() {
        if (indexQuestion >= sessionQuestions.size()) {
            return false;
        }

        return true;
    }

    public Question getNextQuestion() {
        if (!hasNextQuestion()) {
            return null;
        }

        currentQuestion = sessionQuestions.get(indexQuestion);

        if (indexQuestion < sessionQuestions.size()) {
            indexQuestion++;
        }

        return currentQuestion;
    }

    public boolean hasPreviousQuestion() {
        if (indexQuestion <= 0) {
            return false;
        }

        return true;
    }

    public Question getPreviousQuestion() {
        if (!hasPreviousQuestion()) {
            return null;
        }

        currentQuestion = sessionQuestions.get(indexQuestion);

        if (indexQuestion > 0) {
            indexQuestion--;
        }

        return currentQuestion;
    }
}
