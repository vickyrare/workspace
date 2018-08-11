package io.codecrafts.McqPilot.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by waqqas on 8/11/2018.
 */
@Entity
@Table(name="Choice")
public class Choice {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "choice_id", nullable = false)
    private UUID id;

    @Column(name = "choice", columnDefinition="TEXT")
    @Length(min=3, max = 250)
    @NotEmpty(message = "*Please provide a choice")
    private String choice;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
