package io.codecrafts.service;

import io.codecrafts.model.Post;

import java.util.List;
import java.util.UUID;

public interface PostService {
	List<Post> getAll();
	List<Post> findAllInRange(int page, int numItems);
	Post findPost(UUID id);
	void savePost(Post post);
	void deletePost(UUID id);
	List<Post> searchByKeyword(String keyword);
}
