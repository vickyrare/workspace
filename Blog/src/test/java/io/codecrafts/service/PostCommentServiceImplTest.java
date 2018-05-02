package io.codecrafts.service;

import io.codecrafts.model.Post;
import io.codecrafts.model.PostComment;
import io.codecrafts.model.User;
import io.codecrafts.repository.PostCommentRepository;
import io.codecrafts.repository.PostRepository;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by waqqas on 4/25/2018.
 */
@RunWith(SpringRunner.class)
public class PostCommentServiceImplTest {

    @TestConfiguration
    public static class PostCommentServiceImplTestContextConfiguration {

        @Bean
        public PostCommentService postCommentService() {
            return new PostCommentServiceImpl();
        }
    }

    @Autowired
    private PostCommentService postCommentService;

    @MockBean
    private PostCommentRepository postCommentRepository;

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

        Post post = new Post();
        post.setId(UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780556"));
        post.setTitle("How to hack Wii U");
        post.setDescription("I am wondering whether anyone can help me hack my Wii U. My Wii U is currently running 1.5 firmware version.");
        post.setUser(user);
        post.setCreationDate(new Date());
        post.setLastModified(post.getCreationDate());

        PostComment postComment = new PostComment();
        postComment.setId(UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780556"));
        postComment.setContent("Visit http://www.wiiu.guide.org");
        postComment.setUser(user);
        postComment.setPostDate(new Date());
        post.addComment(postComment);

        PostComment postComment2 = new PostComment();
        postComment2.setId(UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780557"));
        postComment2.setContent("Visit http://www.3ds.guide.org");
        postComment2.setUser(user);
        postComment2.setPostDate(new Date());
        post.addComment(postComment2);

        when(postCommentRepository.findOne(post.getId())).thenReturn(postComment);

        List<PostComment> list = new ArrayList<>();
        list.add(postComment);
        list.add(postComment2);

        Page<PostComment> pages = new PageImpl<>(list);

        when(postCommentRepository.findByPostIdOrderByPostDateAsc(post.getId(), new PageRequest(0, 2))).thenReturn(pages);
    }

    @Test
    public void whenValidId_thenPostCommentShouldBeFound() {
        UUID postCommentId = UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780556");
        PostComment found = postCommentService.findPostComment(postCommentId);

        assertThat(found.getId()).isEqualTo(postCommentId);

        verify(postCommentRepository).findOne(postCommentId);
    }

    @Test
    public void whenFindAllPostCommentsForPost_thenAllPostCommentsForPostShouldBeFound() {
        List<PostComment> postComments = postCommentService.findAllInRange(UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780556"),0, 2);

        assertThat(postComments.get(0).getContent()).isEqualTo("Visit http://www.wiiu.guide.org");
        assertThat(postComments.get(1).getContent()).isEqualTo("Visit http://www.3ds.guide.org");

        verify(postCommentRepository).findByPostIdOrderByPostDateAsc(UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780556"),new PageRequest(0, 2));
    }
}
