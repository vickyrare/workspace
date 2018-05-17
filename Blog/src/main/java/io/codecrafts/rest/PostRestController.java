package io.codecrafts.rest;

import io.codecrafts.error.CustomErrorType;
import io.codecrafts.model.Post;
import io.codecrafts.model.Role;
import io.codecrafts.model.User;
import io.codecrafts.rest.annotations.RestApiController;
import io.codecrafts.service.PostService;
import io.codecrafts.service.UserService;
import io.codecrafts.util.Util;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

/**
 * Created by waqqas on 4/22/2018.
 */
@RestApiController
public class PostRestController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/posts")
    public ResponseEntity<List<Post>> listAllUsers(@RequestParam(defaultValue = "1") int page) {
        List<Post> posts = postService.findAllInRange(page - 1, Util.ITEMS_PER_PAGE);
        if (posts.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<Post>>(posts, HttpStatus.OK);
    }

    @GetMapping(value="/posts/{postId}")
    public ResponseEntity<?> findPost(@PathVariable UUID postId) {
        Post post = postService.findPost(postId);
        if (post == null) {
            return new ResponseEntity(new CustomErrorType("Post with id " + postId + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @PostMapping(value = "/posts")
    public ResponseEntity<?> addNewPost(@RequestBody Post post, UriComponentsBuilder ucBuilder) {

        post.setCreationDate(new Date());
        post.setLastModified(post.getCreationDate());

        postService.savePost(post);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/posts/{id}").buildAndExpand(post.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/posts/{postId}")
    public ResponseEntity<?> editPost(@PathVariable UUID postId, @RequestBody Post post) {
        Post currentPost = postService.findPost(postId);

        if (currentPost == null) {
            return new ResponseEntity(new CustomErrorType("Unable to upate. Post with id " + postId + " not found."), HttpStatus.NOT_FOUND);
        }

        currentPost.setTitle(post.getTitle());
        currentPost.setDescription(post.getDescription());
        currentPost.setLastModified(new Date());

        postService.savePost(currentPost);
        return new ResponseEntity<>(currentPost, HttpStatus.OK);
    }

    @DeleteMapping(value = "/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable UUID postId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.findUserByEmail(auth.getName());
        Post post = postService.findPost(postId);
        if (post == null) {
            return new ResponseEntity(new CustomErrorType("Unable to delete. Post with id " + postId + " not found."), HttpStatus.NOT_FOUND);
        }
        if(loggedInUser.isAdmin() || loggedInUser.getId() == post.getUser().getId()) {
            postService.deletePost(postId);
        }
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }
}
