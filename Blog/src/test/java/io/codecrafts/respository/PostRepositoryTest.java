package io.codecrafts.respository;

import io.codecrafts.model.Post;
import io.codecrafts.model.Role;
import io.codecrafts.model.User;
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

import java.util.*;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by waqqas on 4/25/2018.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PostRepository postRepository;

    private TestHelper testHelper = new TestHelper();

    @Test
    public void whenFindById_thenReturnPost() {
        emptyDatabase();
        Post post = testHelper.createPost(userRepository, postRepository);
        Post found = postRepository.findOne(post.getId());

        assertThat(found.getTitle()).isEqualTo(post.getTitle());
        assertThat(found.getCreationDate()).isEqualTo(post.getCreationDate());
        assertThat(found.getDescription()).isEqualTo(post.getDescription());
        assertThat(found.getLastModified()).isEqualTo(post.getLastModified());
        assertThat(found.getUser()).isEqualTo(post.getUser());
    }

    @Test
    public void whenDeleteById_thenReturnDeletePost() {
        emptyDatabase();
        Post post = testHelper.createPost(userRepository, postRepository);
        postRepository.delete(post.getId());

        Post found = postRepository.findOne(post.getId());

        assertThat(found).isEqualTo(null);
    }

    @Test
    public void whenFindAllPostDesc_thenReturnAllPostDesc() {
        emptyDatabase();
        Post post = testHelper.createPost(userRepository, postRepository);
        Post post2 = testHelper.createPost(userRepository, postRepository);

        List<Post> posts = new ArrayList<Post>();
        postRepository.findAllByOrderByLastModifiedDesc(new PageRequest(0, 2)).forEach(posts::add);

        assertThat(posts.get(0).getId()).isEqualTo(post2.getId());
        assertThat(posts.get(1).getId()).isEqualTo(post.getId());
    }

    @Test
    public void whenFindByKeyword_thenReturnPosts() {
        emptyDatabase();
        testHelper.createPost(userRepository, postRepository);

        List<Post> posts = postRepository.findAllByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining("hack", "hack");
        assertTrue(posts.size() == 1);
        assertThat(posts.get(0).getTitle()).isEqualTo("How to hack Wii U");
    }

    @Test
    public void whenFindByKeyword_NoMatch() {
        emptyDatabase();
        testHelper.createPost(userRepository, postRepository);

        List<Post> posts = postRepository.findAllByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining("hack1", "hack1");
        assertTrue(posts.size() == 0);
    }

    private void emptyDatabase() {
        postRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }
}
