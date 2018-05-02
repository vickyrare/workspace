package io.codecrafts.util;

import io.codecrafts.model.Post;
import io.codecrafts.model.PostComment;
import io.codecrafts.model.Role;
import io.codecrafts.model.User;
import io.codecrafts.repository.PostCommentRepository;
import io.codecrafts.repository.PostRepository;
import io.codecrafts.repository.RoleRepository;
import io.codecrafts.repository.UserRepository;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

public class TestHelper {

    public User getUser() {
        Role role = new Role();
        role.setRole("USER");

        User user = new User();
        user.setFirstName("Waqqas");
        user.setLastName("Sharif");
        user.setEmail("vickyrare@yahoo.com");
        user.setPassword("12345678");
        user.setActive(true);
        user.setCreationDate(new Date());
        user.setProfilePicture("avatar.png");
        user.setRoles(new HashSet<>(Arrays.asList(role)));

        return user;
    }

    public User getAdminUser() {
        Role role = new Role();
        role.setRole("ADMIN");

        User user = new User();
        user.setFirstName("Waqqas");
        user.setLastName("Sharif");
        user.setEmail("vickyrare@yahoo.com");
        user.setPassword("12345678");
        user.setActive(true);
        user.setCreationDate(new Date());
        user.setProfilePicture("avatar.png");
        user.setRoles(new HashSet<>(Arrays.asList(role)));

        return user;
    }

    public Post getPost() {
        Post post = new Post();
        post.setTitle("How to hack Wii U");
        post.setDescription("I am wondering whether anyone can help me hack my Wii U. My Wii U is currently running 1.5 firmware version.");
        post.setCreationDate(new Date());
        post.setLastModified(post.getCreationDate());
        return post;
    }

    public PostComment getPostComment() {
        PostComment postComment = new PostComment();
        postComment.setContent("Visit http://www.wiiu.guide.org");
        return postComment;
    }

    public User createUser(UserRepository userRepository, RoleRepository roleRepository) {
        User user = getUser();

        user = userRepository.save(user);
        return user;
    }

    public User createAdminUser(UserRepository userRepository, RoleRepository roleRepository) {
        User adminUser = getAdminUser();

        adminUser = userRepository.save(adminUser);
        return adminUser;
    }

    public Post createPost(UserRepository userRepository, RoleRepository roleRepository, PostRepository postRepository) {
        User user = createUser(userRepository, roleRepository);
        Post post =  getPost();
        post.setUser(user);
        post = postRepository.save(post);
        return post;
    }

    public PostComment createPostComment(UserRepository userRepository, RoleRepository roleRepository, PostRepository postRepository, PostCommentRepository postCommentRepository) {
        Post post = createPost(userRepository, roleRepository, postRepository);
        PostComment postComment = getPostComment();
        postComment.setUser(post.getUser());
        postComment.setPostDate(new Date());
        post.addComment(postComment);
        postCommentRepository.save(postComment);
        return postComment;
    }

    public PostComment createAnotherPostCommentForTheSamePost(Post post, PostCommentRepository postCommentRepository) {
        PostComment postComment = getPostComment();
        postComment.setUser(post.getUser());
        postComment.setPostDate(new Date());
        post.addComment(postComment);
        postCommentRepository.save(postComment);
        return postComment;
    }
}
