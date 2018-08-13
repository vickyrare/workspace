package io.codecrafts.McqPilot;

import io.codecrafts.McqPilot.lib.Application;
import io.codecrafts.McqPilot.lib.QuestionProcessor;
import io.codecrafts.McqPilot.model.Question;
import io.codecrafts.McqPilot.service.QuestionService;
import io.codecrafts.McqPilot.util.QuestionDetailsReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
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

		Application application = new Application(questionProcessor, showResultsProgressively);
		application.startTest();
	}
}
