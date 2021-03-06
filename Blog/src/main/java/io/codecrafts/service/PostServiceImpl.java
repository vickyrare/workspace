package io.codecrafts.service;

import io.codecrafts.model.Post;
import io.codecrafts.model.User;
import io.codecrafts.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;

	@Override
	public List<Post> getAll() {
		List<Post> posts = new ArrayList<Post>();
		postRepository.findAll().forEach(posts::add);
		return posts;
	}

	public List<Post> findAllInRange(int page, int numItems){
		List<Post> posts = new ArrayList<Post>();
		postRepository.findAllByOrderByLastModifiedDesc(new PageRequest(page, numItems)).forEach(posts::add);

		return posts;
	}

	@Override
	public void savePost(Post post) {
		postRepository.save(post);
	}

    public Post findPost(UUID id) {
	    Post post = postRepository.findOne(id);
	    return post;
    }

	@Override
	public void deletePost(UUID id) {
		postRepository.delete(id);
	}

	@Override
	public List<Post> searchByKeyword(String keyword, Pageable pageable) {
		if(pageable == null) {
			return postRepository.findAllByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(keyword, keyword);
		}
		return postRepository.findAllByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(keyword, keyword, pageable);
	}
}
