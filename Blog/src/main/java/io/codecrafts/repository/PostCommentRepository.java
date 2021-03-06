package io.codecrafts.repository;

import io.codecrafts.model.Post;
import io.codecrafts.model.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, UUID> {
    public List<PostComment> findByPostId(UUID postId);
    Page<PostComment> findByPostId(UUID postId, Pageable pageable);
    Page<PostComment> findByPostIdOrderByPostDateAsc(UUID postId, Pageable pageable);
}
