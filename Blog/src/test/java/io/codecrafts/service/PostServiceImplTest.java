package io.codecrafts.service;

import io.codecrafts.model.Post;
import io.codecrafts.model.Role;
import io.codecrafts.model.User;
import io.codecrafts.repository.PostRepository;
import io.codecrafts.repository.RoleRepository;
import io.codecrafts.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by waqqas on 4/25/2018.
 */
@RunWith(SpringRunner.class)
public class PostServiceImplTest {

    @TestConfiguration
    public static class PostServiceImplTestContextConfiguration {

        @Bean
        public PostService postService() {
            return new PostServiceImpl();
        }
    }

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @Before
    public void setUp() {
        User user = new User();

        user.setId(UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780556"));
        user.setFirstName("Waqqas");
        user.setLastName("Sharif");
        user.setEmail("vickyrare@yahoo.com");
        user.setPassword("12345678");
        user.setActive(true);
        user.setCreationDate(new Date());
        user.setProfilePicture("avatar.png");

        Post post =  new Post();
        post.setId(UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780556"));
        post.setTitle("How to hack Wii U");
        post.setDescription("I am wondering whether anyone can help me hack my Wii U. My Wii U is currently running 1.5 firmware version.");
        post.setUser(user);
        post.setCreationDate(new Date());
        post.setLastModified(post.getCreationDate());

        Post post2 =  new Post();
        post2.setId(UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780557"));
        post2.setTitle("How to hack 3DS");
        post2.setDescription("I am wondering whether anyone can help me hack my 3DS. My 3DS is currently running 4.0 firmware version.");
        post2.setUser(user);
        post2.setCreationDate(new Date());
        post2.setLastModified(post.getCreationDate());

        when(postRepository.findOne(post.getId())).thenReturn(post);

        List<Post> list = new ArrayList<>();
        list.add(post2);
        list.add(post);

        Page<Post> pages = new PageImpl<>(list);

        when(postRepository.findAllByOrderByLastModifiedDesc(new PageRequest(0, 2))).thenReturn(pages);

        when(postRepository.findAllByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining("hack", "hack", new PageRequest(0, 3))).thenReturn(list);

        when(postRepository.findAllByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining("hack1", "hack1", new PageRequest(0, 3))).thenReturn(null);
    }

    @Test
    public void whenValidId_thenPostShouldBeFound() {
        UUID postId = UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780556");
        Post found = postService.findPost(postId);

        assertThat(found.getId()).isEqualTo(postId);

        verify(postRepository).findOne(postId);
    }

    @Test
    public void whenFindAll_thenAllPostsShouldBeFound() {
        List<Post> posts = postService.findAllInRange(0, 2);

        assertThat(posts.get(0).getTitle()).isEqualTo("How to hack 3DS");
        assertThat(posts.get(1).getTitle()).isEqualTo("How to hack Wii U");

        verify(postRepository).findAllByOrderByLastModifiedDesc(new PageRequest(0, 2));
    }


    @Test
    public void whenFindByKeyword_thenPostsShouldBeFound() {
        List<Post> posts = postService.searchByKeyword("hack", new PageRequest(0, 3));

        assertThat(posts.get(0).getTitle()).isEqualTo("How to hack 3DS");
        assertThat(posts.get(1).getTitle()).isEqualTo("How to hack Wii U");

        verify(postRepository).findAllByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining("hack", "hack", new PageRequest(0, 3));
    }

    @Test
    public void whenFindByKeyword_thenNoMatch() {
        List<Post> posts = postService.searchByKeyword("hack1", new PageRequest(0, 3));

        assertTrue(posts == null);

        verify(postRepository).findAllByTitleIgnoreCaseContainingOrDescriptionIgnoreCaseContaining("hack1", "hack1", new PageRequest(0, 3));
    }
}
