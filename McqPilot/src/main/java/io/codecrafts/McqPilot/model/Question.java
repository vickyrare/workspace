package io.codecrafts.McqPilot.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by waqqas on 8/11/2018.
 */
@Entity
@Table(name="Question")
public class Question {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "question_id", nullable = false)
    private UUID id;

    @Column(name = "question", columnDefinition="TEXT")
    @Length(min=3, max = 250)
    @NotEmpty(message = "*Please provide a question")
    private String question;

    @Column(name = "answer")
    @NotEmpty(message = "*Please provide a answer")
    private Character answer;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Choice> choices = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public Character getAnswer() {
        return answer;
    }

    public void setAnswer(Character answer) {
        this.answer = answer;
    }

    public void setId(UUID id) {

        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}
