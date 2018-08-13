package io.codecrafts.McqPilot.lib;

public class ResultProcessor {
    private int numCorrectAnswers;
    private int numIncorrectAnswers;
    private int currentQuestionIndex;
    private double percentageCorrect;

    public ResultProcessor() {
        numCorrectAnswers = 0;
        numIncorrectAnswers = 0;
        currentQuestionIndex = 0;
        percentageCorrect = 0.0;
    }

    public void reinit() {
        numCorrectAnswers = 0;
        numIncorrectAnswers = 0;
        currentQuestionIndex = 0;
        percentageCorrect = 0.0;
    }

    public int getNumCorrectAnswer() {
        return numCorrectAnswers;
    }

    public void incrementCorrectAnswer() {
        numCorrectAnswers++;
    }

    public int getNumIncorrectAnswers() {
        return numIncorrectAnswers;
    }

    public void incrementIncorrectAnswer() {
        numIncorrectAnswers++;
    }

    public double getPercentageCorrect() {
        if (currentQuestionIndex == 0) {
            return 0.0;
        }

        if (numCorrectAnswers == currentQuestionIndex) {
            return 100.0;
        }

        percentageCorrect = (numCorrectAnswers * 100) / (currentQuestionIndex);
        return percentageCorrect;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void incrementCurrentQuestionIndex() {
        currentQuestionIndex++;
    }
}
