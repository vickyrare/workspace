package io.codecrafts.service;

import io.codecrafts.model.Post;
import io.codecrafts.model.Role;
import io.codecrafts.model.User;
import io.codecrafts.repository.PostRepository;
import io.codecrafts.repository.RoleRepository;
import io.codecrafts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		postRepository.findAll(new PageRequest(page, numItems)).forEach(posts::add);
		return posts;
	}

	@Override
	public void savePost(Post post) {
		postRepository.save(post);
	}

    public Post findPost(Long id) {
	    Post post = postRepository.findOne(id);
	    return post;
    }

	@Override
	public void deletePost(Long id) {
		postRepository.delete(id);
	}
}
