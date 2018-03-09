package io.codecrafts.service;

import io.codecrafts.model.Post;
import io.codecrafts.model.PostComment;

import java.util.ArrayList;
import java.util.List;

public interface PostCommentService {
	public List<PostComment> getAllComments(Long id);
	public List<PostComment> findAllInRange(Long postId, int page, int numItems);
	public PostComment findPostComment(Long id);
	public PostComment getComment(Long id);
	public void addComment(PostComment postComment);
	public void updateComment(PostComment postComment);
	public void deleteComment(Long id);
}
