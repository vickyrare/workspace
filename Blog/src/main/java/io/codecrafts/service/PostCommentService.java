package io.codecrafts.service;

import io.codecrafts.model.Post;
import io.codecrafts.model.PostComment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface PostCommentService {
	public List<PostComment> getAllComments(UUID id);
	public List<PostComment> findAllInRange(UUID postId, int page, int numItems);
	public PostComment findPostComment(UUID id);
	public PostComment getComment(UUID id);
	public void addComment(PostComment postComment);
	public void updateComment(PostComment postComment);
	public void deleteComment(UUID id);
}
