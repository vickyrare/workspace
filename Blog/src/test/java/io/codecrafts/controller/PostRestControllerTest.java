package io.codecrafts.controller;

import io.codecrafts.model.Post;
import io.codecrafts.model.Role;
import io.codecrafts.model.User;
import io.codecrafts.repository.RoleRepository;
import io.codecrafts.rest.PostRestController;
import io.codecrafts.rest.UserRestController;
import io.codecrafts.service.PostService;
import io.codecrafts.service.UserService;
import io.codecrafts.util.JsonUtil;
import io.codecrafts.util.TestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by waqqas on 4/25/2018.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = PostRestController.class, secure = false)
public class PostRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostService postService;

    @MockBean
    private UserService userService;

    private TestHelper testHelper = new TestHelper();

    @Test
    public void givenPosts_whenGetPosts() throws Exception {
        User user = testHelper.getUser();

        Post post = testHelper.getPost();
        post.setUser(user);
        post.setLastModified(post.getCreationDate());

        List<Post> allPosts = Arrays.asList(post);

        given(postService.findAllInRange(0, 10)).willReturn(allPosts);

        mvc.perform(get("/api/posts")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(post.getTitle())));
        verify(postService).findAllInRange(0, 10);
    }

    @Test
    public void whenPostPost_thenCreatePost() throws Exception {
        User user = testHelper.getUser();
        Post post = testHelper.getPost();
        post.setUser(user);
        post.setLastModified(post.getCreationDate());

        mvc.perform(post("/api/posts")
                            .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(post.getTitle())))
                .andExpect(jsonPath("$.description", is(post.getDescription())));
    }

    @Test
    public void whenEditPost_thenEditPost() throws Exception {
        UUID postId = UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780556");

        Post post = new Post();
        post.setTitle("Test Post");
        post.setDescription("Test Post Description");

        given(postService.findPost(postId)).willReturn(post);

        mvc.perform(put("/api/posts/" + postId)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(post.getTitle())));

        verify(postService).findPost(postId);
    }

    @Test
    public void whenFindPost_thenReturnPost() throws Exception {
        UUID postId = UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780556");

        Post post = testHelper.getPost();

        given(postService.findPost(postId)).willReturn(post);

        mvc.perform(get("/api/posts/" + postId)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is(post.getTitle())));
        verify(postService).findPost(postId);
    }
}
