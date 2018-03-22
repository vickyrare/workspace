package io.codecrafts.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "post_id")
	private Long id;

	@Column(name = "title")
	@NotEmpty(message = "*Please provide a Title")
	private String title;

	@Column(name="creation_date")
	@Temporal(TemporalType.TIMESTAMP)
	private	Date creationDate;

	@Column(name="last_modified")
	@Temporal(TemporalType.TIMESTAMP)
	private	Date lastModified;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PostComment> postComments = new ArrayList<>();

	public void addComment(PostComment comment) {
		postComments.add(comment);
		comment.setPost(this);
	}

	public void removeComment(PostComment comment) {
		postComments.remove(comment);
		comment.setPost(null);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

    public List<PostComment> getPostComments() {
        return postComments;
    }

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
}
