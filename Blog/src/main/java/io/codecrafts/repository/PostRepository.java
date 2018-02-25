package io.codecrafts.repository;

import io.codecrafts.model.Post;
import io.codecrafts.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	Post findByTitle(String title);
}
