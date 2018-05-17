package io.codecrafts.controller;

import io.codecrafts.model.Post;
import io.codecrafts.model.PostComment;
import io.codecrafts.model.User;
import io.codecrafts.rest.PostCommentRestController;
import io.codecrafts.rest.PostRestController;
import io.codecrafts.service.PostCommentService;
import io.codecrafts.service.PostService;
import io.codecrafts.service.UserService;
import io.codecrafts.util.JsonUtil;
import io.codecrafts.util.TestHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by waqqas on 4/25/2018.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = PostCommentRestController.class, secure = false)
public class PostCommentRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostService postService;

    @MockBean
    private UserService userService;

    @MockBean
    private PostCommentService postCommentService;

    private TestHelper testHelper = new TestHelper();

    @Test
    public void givenPostComments_whenGetPostComments() throws Exception {
        UUID postId = UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780556");

        User user = testHelper.getUser();
        Post post = testHelper.getPost();
        post.setId(postId);
        post.setUser(user);
        post.setLastModified(post.getCreationDate());

        PostComment postComment = testHelper.getPostComment();
        postComment.setUser(user);
        postComment.setPostDate(new Date());
        post.addComment(postComment);

        List<PostComment> allPostComments = Arrays.asList(postComment);

        given(postCommentService.getAllComments(postId)).willReturn(allPostComments);

        mvc.perform(get("/api/posts/" + postId + "/comments")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].content", is(postComment.getContent())));
        verify(postCommentService).getAllComments(postId);
    }

    @Test
    public void whenPostPostComment_thenCreatePostComment() throws Exception {
        UUID postId = UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780556");

        User user = testHelper.getUser();
        Post post = testHelper.getPost();
        post.setId(postId);
        post.setUser(user);
        post.setLastModified(post.getCreationDate());

        PostComment postComment = testHelper.getPostComment();
        postComment.setUser(user);
        postComment.setPostDate(new Date());
        post.addComment(postComment);

        given(postService.findPost(postId)).willReturn(post);

        mvc.perform(post("/api/posts/" + postId + "/comments")
                            .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(post)))
                .andExpect(status().is2xxSuccessful());
        verify(postService).findPost(postId);
    }

    @Test
    public void whenFindPostComment_thenReturnPostComment() throws Exception {
        UUID postCommentId = UUID.fromString("4ca1e527-d84b-49fc-a033-e27c59780556");

        User user = testHelper.getUser();
        Post post = testHelper.getPost();
        post.setUser(user);
        post.setLastModified(post.getCreationDate());

        PostComment postComment = testHelper.getPostComment();
        postComment.setUser(user);
        postComment.setPostDate(new Date());
        post.addComment(postComment);

        given(postCommentService.findPostComment(postCommentId)).willReturn(postComment);

        mvc.perform(get("/api/posts/" + postCommentId + "/comments/" + postCommentId)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is(postComment.getContent())));
        verify(postCommentService).findPostComment(postCommentId);
    }
}
