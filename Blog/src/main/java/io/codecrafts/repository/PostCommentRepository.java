package io.codecrafts.repository;

import io.codecrafts.model.Post;
import io.codecrafts.model.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    public List<PostComment> findByPostId(Long postId);
    Page<PostComment> findByPostId(Long postId, Pageable pageable);
}
