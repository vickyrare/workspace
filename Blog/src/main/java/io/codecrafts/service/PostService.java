package io.codecrafts.service;

import io.codecrafts.model.Post;

import java.util.List;

public interface PostService {
	public List<Post> getAll();
	public Post findPost(Long id);
	public void savePost(Post post);
	public void deletePost(Long id);
}
