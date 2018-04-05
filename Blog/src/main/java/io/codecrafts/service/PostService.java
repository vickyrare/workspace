package io.codecrafts.service;

import io.codecrafts.model.Post;

import java.util.List;
import java.util.UUID;

public interface PostService {
	public List<Post> getAll();
	public List<Post> findAllInRange(int page, int numItems);
	public Post findPost(UUID id);
	public void savePost(Post post);
	public void deletePost(UUID id);
}
