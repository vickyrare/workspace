package io.codecrafts.respository;

import io.codecrafts.model.Post;
import io.codecrafts.model.PostComment;
import io.codecrafts.repository.PostCommentRepository;
import io.codecrafts.repository.PostRepository;
import io.codecrafts.repository.RoleRepository;
import io.codecrafts.repository.UserRepository;
import io.codecrafts.util.TestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by waqqas on 4/25/2018.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class PostCommentRespositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostCommentRepository postCommentRepository;

    private TestHelper testHelper = new TestHelper();

    @Test
    public void whenFindPostCommentByPostId_thenReturnPostComment() {
        PostComment postComment = testHelper.createPostComment(userRepository, roleRepository, postRepository, postCommentRepository);

        Post post = postComment.getPost();
        Post found = postRepository.findOne(post.getId());

        assertThat(found.getPostComments().get(0).getContent()).isEqualTo(post.getPostComments().get(0).getContent());
        assertThat(found.getPostComments().get(0).getPostDate()).isEqualTo(post.getPostComments().get(0).getPostDate());
    }

    @Test
    public void whenDeleteCommentById_thenReturnDeletePost() {
        PostComment postComment = testHelper.createPostComment(userRepository, roleRepository, postRepository, postCommentRepository);

        postCommentRepository.delete(postComment.getId());

        PostComment found = postCommentRepository.findOne(postComment.getId());

        assertThat(found).isEqualTo(null);
    }
}
