package io.codecrafts.McqPilot.lib;

import io.codecrafts.McqPilot.model.Choice;
import io.codecrafts.McqPilot.model.Question;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Application {
    private QuestionProcessor questionProcessor;
    private ResultProcessor resultProcessor;
    private boolean showResultsProgressively;
    private Scanner reader = new Scanner(System.in);
    List<Character> allowedChars = new ArrayList<>();
    private Question currentQuestion;

    public Application(QuestionProcessor questionProcessor, boolean showResultsProgressively) {
        this.questionProcessor = questionProcessor;
        this.resultProcessor = new ResultProcessor();
        this.showResultsProgressively = showResultsProgressively;
        allowedChars.add('A');
        allowedChars.add('B');
        allowedChars.add('C');
        allowedChars.add('D');
    }

    public void displayResults() {
        System.out.println(MessageFormat.format("Correct: {0}, Incorrect: {1}, Percent Correct: %{2}", resultProcessor.getNumCorrectAnswer(), resultProcessor.getNumIncorrectAnswers(), resultProcessor.getPercentageCorrect()));
    }

    public void displayProgress() {
        if (showResultsProgressively) {
            System.out.println(MessageFormat.format("Progress is {0} / {1}, Correct: {2}, Incorrect: {3}, Percent Correct: %{4}", (resultProcessor.getCurrentQuestionIndex() + 1), questionProcessor.getNumSessionQuestions(), resultProcessor.getNumCorrectAnswer(), resultProcessor.getNumIncorrectAnswers(), resultProcessor.getPercentageCorrect()));
        }
    }

    public void displayCurrentQuestion() {
        currentQuestion = questionProcessor.getNextQuestion();
        String questionText = currentQuestion.getQuestion();
        String []questionLines = questionText.split("\n");

        for (String text: questionLines) {
            System.out.println(text);
        }

        for (Choice choice: currentQuestion.getChoices()) {
            String choiceText = choice.getChoice();
            System.out.println(choiceText);
        }
    }

    public void inputAndProcessAnswer() {
        System.out.println("Enter answer (A, B, C, D): ");
        char c = reader.next().charAt(0);

        c = Character.toUpperCase(c);

        while (!allowedChars.contains(c)) {
            System.out.println("Enter answer (A, B, C, D): ");
            c = reader.next().charAt(0);
        }

        if (c == currentQuestion.getAnswer().charValue()) {
            resultProcessor.incrementCorrectAnswer();
        } else {
            resultProcessor.incrementIncorrectAnswer();
            System.out.println("Correct answer: " + currentQuestion.getAnswer());
        }

        resultProcessor.incrementCurrentQuestionIndex();
        System.out.println();
    }

    public void startTest() {
        while (questionProcessor.hasNextQuestion()) {
            displayProgress();
            displayCurrentQuestion();
            inputAndProcessAnswer();
        }
        displayResults();
    }
}
