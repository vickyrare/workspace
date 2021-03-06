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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by waqqas on 4/25/2018.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class PostCommentRepositoryTest {

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
        emptyDatabase();
        PostComment postComment = testHelper.createPostComment(roleRepository, userRepository, postRepository, postCommentRepository);

        Post post = postComment.getPost();
        Post found = postRepository.findOne(post.getId());

        assertThat(found.getPostComments().get(0).getContent()).isEqualTo(post.getPostComments().get(0).getContent());
        assertThat(found.getPostComments().get(0).getPostDate()).isEqualTo(post.getPostComments().get(0).getPostDate());
    }

    @Test
    public void whenDeleteCommentById_thenReturnDeletePost() {
        emptyDatabase();
        PostComment postComment = testHelper.createPostComment(roleRepository, userRepository, postRepository, postCommentRepository);

        postCommentRepository.delete(postComment.getId());

        PostComment found = postCommentRepository.findOne(postComment.getId());

        assertThat(found).isEqualTo(null);
    }

    @Test
    public void whenFindAllPostCommentsAsc_thenReturnAllPostCommentsAsc() throws InterruptedException {
        emptyDatabase();
        PostComment postComment = testHelper.createPostComment(roleRepository, userRepository, postRepository, postCommentRepository);

        //making sure that the two postComments has a time between inserts otherwise sometimes the findByPostIdOrderByPostDateAsc fails
        Thread.sleep(5);

        PostComment postComment2 = testHelper.createAnotherPostCommentForTheSamePost(postComment.getPost(), postCommentRepository);

        List<PostComment> postComments = new ArrayList<>();
        postCommentRepository.findByPostIdOrderByPostDateAsc(postComment.getPost().getId(), new PageRequest( 0, 2)).forEach(postComments::add);

        assertThat(postComments.get(0).getId()).isEqualTo(postComment.getId());
        assertThat(postComments.get(1).getId()).isEqualTo(postComment2.getId());
    }

    private void emptyDatabase() {
        postCommentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }
}
