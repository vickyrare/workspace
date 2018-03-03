package io.codecrafts.service;

import io.codecrafts.model.PostComment;
import io.codecrafts.repository.PostCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostCommentServiceImpl implements PostCommentService {

	@Autowired
	private PostCommentRepository postCommentRepository;

	public List<PostComment> getAllComments(Long postId) {
		List<PostComment> postComments = new ArrayList<>();
		postCommentRepository.findByPostId(postId).forEach(postComments::add);
		return postComments;
	}

	public PostComment getComment(Long id) {
		return postCommentRepository.findOne(id);
	}

	public void addComment(PostComment postComment) {
		postCommentRepository.save(postComment);
	}

	public void updateComment(PostComment postComment) {
		postCommentRepository.save(postComment);
	}

	public void deleteComment(Long id) {
		postCommentRepository.delete(id);
	}
	public PostComment findPostComment(Long id) {
	    PostComment postComment = postCommentRepository.findOne(id);
	    return postComment;
    }
}
