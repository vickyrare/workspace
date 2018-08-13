package io.codecrafts.McqPilot;

import io.codecrafts.McqPilot.lib.QuestionProcessor;
import io.codecrafts.McqPilot.lib.ResultProcessor;
import io.codecrafts.McqPilot.model.Choice;
import io.codecrafts.McqPilot.model.Question;
import io.codecrafts.McqPilot.service.QuestionService;
import io.codecrafts.McqPilot.util.QuestionDetailsReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class McqPilotApplication implements CommandLineRunner{

	@Autowired
	private QuestionDetailsReader questionDetailsReader;

	@Autowired
	private QuestionService questionService;

	public static void main(String[] args) {
		SpringApplication.run(McqPilotApplication.class, args);
	}

	@Value("${question.details.file}")
	private String questionDetailsFileLocation;

	@Override
	public void run(String... args) throws Exception {

		String strArgs = Arrays.stream(args).collect(Collectors.joining("|"));
		System.out.println("Application started with arguments:" + strArgs);

		boolean randomizeQuestions = false;
		boolean showResultsProgressively = false;

		List<Character> allowedChars = new ArrayList<>();
		allowedChars.add('A');
		allowedChars.add('B');
		allowedChars.add('C');
		allowedChars.add('D');

		List<Question> questions = questionService.getAll();

		int nQuestions = questions.size();

		if (questions.size() == 0) {
			questionDetailsReader.readFromFile(new File(questionDetailsFileLocation));
		}

		for(String arg: args) {
			if(arg.compareTo("--randomizeQuestions") == 0) {
				randomizeQuestions = true;
			}

			if(arg.startsWith("--nQuestions")) {
				nQuestions = Integer.parseInt(arg.replace("--nQuestions=", ""));
				if (nQuestions > questions.size()) {
					nQuestions = questions.size();
				}
			}

			if(arg.compareTo("--showResultsProgressively") == 0) {
				showResultsProgressively = true;
			}
		}

		QuestionProcessor questionProcessor = new QuestionProcessor(nQuestions, questions, randomizeQuestions);
		questionProcessor.init();

		ResultProcessor resultProcessor = new ResultProcessor();

		while (questionProcessor.hasNextQuestion()) {
			if (showResultsProgressively) {
				System.out.println(MessageFormat.format("Progress is {0} / {1}, Correct: {2}, Incorrect: {3}, Percent Correct: %{4}", (resultProcessor.getCurrentQuestionIndex() + 1), questionProcessor.getNumSessionQuestions(), resultProcessor.getNumCorrectAnswer(), resultProcessor.getNumIncorrectAnswers(), resultProcessor.getPercentageCorrect()));
			}

			Question question = questionProcessor.getNextQuestion();
			String questionText = question.getQuestion();
			String []questionLines = questionText.split("\n");

			for (String text: questionLines) {
				System.out.println(text);
			}

			for (Choice choice: question.getChoices()) {
				String choiceText = choice.getChoice();
				System.out.println(choiceText);
			}

			Scanner reader = new Scanner(System.in);
			System.out.println("Enter answer (A, B, C, D): ");
			char c = reader.next().charAt(0);

			c = Character.toUpperCase(c);

			while (!allowedChars.contains(c)) {
				System.out.println("Enter answer (A, B, C, D): ");
				c = reader.next().charAt(0);
			}

			if (c == question.getAnswer().charValue()) {
				resultProcessor.incrementCorrectAnswer();
			} else {
				resultProcessor.incrementIncorrectAnswer();
				System.out.println("Correct answer: " + question.getAnswer());
			}

			resultProcessor.incrementCurrentQuestionIndex();
			System.out.println();
		}
		System.out.println(MessageFormat.format("Correct: {0}, Incorrect: {1}, Percent Correct: %{2}", resultProcessor.getNumCorrectAnswer(), resultProcessor.getNumIncorrectAnswers(), resultProcessor.getPercentageCorrect()));
	}
}
