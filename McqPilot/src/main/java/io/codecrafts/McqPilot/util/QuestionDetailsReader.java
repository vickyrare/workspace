package io.codecrafts.McqPilot.util;

import io.codecrafts.McqPilot.model.Choice;
import io.codecrafts.McqPilot.model.Question;
import io.codecrafts.McqPilot.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by waqqas on 8/11/2018.
 */
@Component
public class QuestionDetailsReader {

    @Autowired
    private QuestionService questionService;

    public void readFromFile(File file) {
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Question question = null;
        String questionText = "";
        String answer = "";

        List<String> choices = new ArrayList<>();
        while (sc.hasNextLine()) {
            String currentLine = sc.nextLine();
            if (currentLine.startsWith("Question)")) {
                questionText += currentLine.replace("Question)", "") + "\n";
            }

            if (currentLine.startsWith("Answer)")) {
                answer = currentLine;
                answer = answer.replace("Answer)Correct Answer: ", "");
            }

            if (currentLine.startsWith("Choice)")) {
                String choiceText = currentLine;
                choiceText = choiceText.replace("Choice)", "");
                choices.add(choiceText);
            }

            if(currentLine.isEmpty()) {
                question = new Question();
                question.setQuestion(questionText);

                for(String choiceText: choices) {
                    Choice choice = new Choice();
                    choice.setChoice(choiceText);
                    question.getChoices().add(choice);
                    choice.setQuestion(question);
                }

                question.setAnswer(answer.charAt(0));
                questionService.saveQuestion(question);
                questionText = "";
                choices = new ArrayList<>();
            }
        }
    }
}
