package io.codecrafts.rest;

import io.codecrafts.error.CustomErrorType;
import io.codecrafts.model.Post;
import io.codecrafts.model.PostComment;
import io.codecrafts.model.User;
import io.codecrafts.rest.annotations.RestApiController;
import io.codecrafts.service.PostCommentService;
import io.codecrafts.service.PostService;
import io.codecrafts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity<?> getAllComments(@PathVariable UUID postId) {
        List<PostComment> postComments = postCommentService.getAllComments(postId);
        if (postComments.isEmpty()) {
            return new ResponseEntity(new CustomErrorType("Post with id " + postId + " don't have ant comments"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(postComments, HttpStatus.OK);
    }

    @GetMapping(value="/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> findComment(@PathVariable UUID commentId){
        PostComment postComment = postCommentService.findPostComment(commentId);
        if (postComment == null) {
            return new ResponseEntity(new CustomErrorType("Comment with id " + commentId + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postComment, HttpStatus.OK);
    }

    @PostMapping(value="/posts/{postId}/comments")
    public ResponseEntity<?> addPostComment(@PathVariable UUID postId, @RequestBody PostComment postComment, UriComponentsBuilder ucBuilder) {

        Post post = postService.findPost(postId);
        if (post == null) {
            return new ResponseEntity(new CustomErrorType("Post with id " + postId + " not found"), HttpStatus.NOT_FOUND);
        }

        PostComment newPostComment = new PostComment();
        newPostComment.setContent(postComment.getContent());
        newPostComment.setUser(postComment.getUser());
        newPostComment.setPostDate(new Date());
        post.addComment(newPostComment);
        post.setLastModified(newPostComment.getPostDate());
        postService.savePost(post);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/posts/{postId}/comments").buildAndExpand(post.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @PutMapping(value="/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> editPost(@PathVariable UUID postId, @PathVariable UUID commentId, @RequestBody PostComment postComment) {
        Post post = postService.findPost(postId);
        if (post == null) {
            return new ResponseEntity(new CustomErrorType("Post with id " + postId + " not found"), HttpStatus.NOT_FOUND);
        }

        PostComment editPostComment = postCommentService.findPostComment(commentId);
        if (editPostComment == null) {
            return new ResponseEntity(new CustomErrorType("Comment with id " + commentId + " not found"), HttpStatus.NOT_FOUND);
        }

        postCommentService.updateComment(editPostComment);
        post = postService.findPost(postId);
        post.setLastModified(editPostComment.getPostDate());
        postService.savePost(post);

        return new ResponseEntity<>(postService, HttpStatus.OK);
    }

    @DeleteMapping(value="/posts/{postId}/comments/{commentId}")
    public ResponseEntity<?> deletePostComment(@PathVariable UUID postId, @PathVariable UUID commentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.findUserByEmail(auth.getName());
        Post post = postService.findPost(postId);
        if (post == null) {
            return new ResponseEntity(new CustomErrorType("Post with id " + postId + " not found"), HttpStatus.NOT_FOUND);
        }

        PostComment postComment = postCommentService.findPostComment(commentId);
        if (postComment == null) {
            return new ResponseEntity(new CustomErrorType("Unable to delete. Comment with id " + postId + " not found."), HttpStatus.NOT_FOUND);
        }

        if(loggedInUser.isAdmin() || loggedInUser.getId() == postComment.getUser().getId()) {
            postCommentService.deleteComment(commentId);
        }
        return new ResponseEntity<PostComment>(HttpStatus.NO_CONTENT);
    }
}
