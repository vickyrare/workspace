package io.codecrafts.rest;

import io.codecrafts.model.Post;
import io.codecrafts.model.User;
import io.codecrafts.rest.annotations.RestApiController;
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
public class PostRestController {

    static int ITEMS_PER_PAGE = 10;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping(value="/posts")
    public List<Post> findAll(@RequestParam(defaultValue = "1")int page){
        List<Post> posts = postService.findAllInRange(page - 1, ITEMS_PER_PAGE);
        return posts;
    }

    @GetMapping(value="/posts/{postId}")
    public Post findPost(@PathVariable UUID postId){
        Post post = postService.findPost(postId);
        return post;
    }

    @PostMapping(value="/posts")
    public Post addNewPost(@RequestBody Post post){
        Post newPost = new Post();
        newPost.setTitle(post.getTitle());
        newPost.setDescription(post.getDescription());
        newPost.setUser(post.getUser());
        newPost.setCreationDate(new Date());
        newPost.setLastModified(newPost.getCreationDate());
        postService.savePost(newPost);
        return newPost;
    }

    @PutMapping(value="/posts/{postId}")
    public Post editPost(@RequestBody Post post, @PathVariable UUID postId){
        Post editPost = postService.findPost(postId);
        editPost.setTitle(post.getTitle());
        editPost.setDescription(post.getDescription());
        editPost.setLastModified(new Date());
        postService.savePost(editPost);
        return editPost;
    }

    @DeleteMapping(value="/posts/{postId}")
    public void deletePost(@PathVariable UUID postId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.findUserByEmail(auth.getName());
        Post post = postService.findPost(postId);

        if(loggedInUser.isAdmin() || loggedInUser.getId() == post.getUser().getId()) {
            postService.deletePost(postId);
        }
    }
}
