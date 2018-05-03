package io.codecrafts.rest;

import io.codecrafts.model.Post;
import io.codecrafts.model.PostComment;
import io.codecrafts.model.User;
import io.codecrafts.rest.annotations.RestApiController;
import io.codecrafts.service.PostCommentService;
import io.codecrafts.service.PostService;
import io.codecrafts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by waqqas on 4/22/2018.
 */
@RestApiController
public class PostCommentRestController {

    @Autowired
    private PostCommentService postCommentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping(value="/posts/{postId}/comments")
    public List<PostComment> getAllComments(@PathVariable UUID postId){
        List<PostComment> postComments = postCommentService.getAllComments(postId);
        return postComments;
    }

    @GetMapping(value="/posts/{postId}/comments/{commentId}")
    public PostComment findComment(@PathVariable UUID commentId){
        PostComment postComment = postCommentService.findPostComment(commentId);
        return postComment;
    }

    @PostMapping(value="/posts/{postId}/comments")
    public void addPostComment(@RequestBody PostComment postComment, @PathVariable UUID postId){
        Post post = postService.findPost(postId);
        PostComment newPostComment = new PostComment();
        newPostComment.setContent(postComment.getContent());
        newPostComment.setUser(postComment.getUser());
        newPostComment.setPostDate(new Date());
        post.addComment(newPostComment);
        post.setLastModified(newPostComment.getPostDate());
        postService.savePost(post);
    }

    @PutMapping(value="/posts/{postId}/comments/{commentId}")
    public void editPostComment(@RequestBody PostComment postComment, @PathVariable UUID postId, @PathVariable UUID commentId){
        PostComment editPostComment = postCommentService.findPostComment(commentId);
        editPostComment.setContent(postComment.getContent());
        postCommentService.updateComment(editPostComment);
        Post post = postService.findPost(postId);
        post.setLastModified(editPostComment.getPostDate());
        postService.savePost(post);
    }

    @DeleteMapping(value="/posts/{postId}/comments/{commentId}")
    public void deletePost(@PathVariable UUID commentId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.findUserByEmail(auth.getName());

        PostComment postComment = postCommentService.findPostComment(commentId);
        if(loggedInUser.isAdmin() || loggedInUser.getId() == postComment.getUser().getId()) {
            postCommentService.deleteComment(commentId);
        }
    }
}
