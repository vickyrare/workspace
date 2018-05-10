package io.codecrafts.repository;

import io.codecrafts.model.Post;
import io.codecrafts.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
	Post findByTitle(String title);
	Page<Post> findAllByOrderByLastModifiedDesc(Pageable pageable);
	List<Post> findAllByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(String title, String description, Pageable pageable);
}
