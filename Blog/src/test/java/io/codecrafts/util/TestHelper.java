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

    public User createUser(UserRepository userRepository, RoleRepository roleRepository) {
        User user = new User();
        user.setFirstName("Waqqas");
        user.setLastName("Sharif");
        user.setEmail("vickyrare@yahoo.com");
        user.setPassword("12345678");
        user.setActive(true);
        user.setCreationDate(new Date());
        user.setProfilePicture("avatar.png");

        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));

        user = userRepository.save(user);
        return user;
    }

    public User createAdminUser(UserRepository userRepository, RoleRepository roleRepository) {
        User adminUser = new User();
        adminUser.setFirstName("Waqqas");
        adminUser.setLastName("Sharif");
        adminUser.setEmail("vickyrare@gmail.com");
        adminUser.setPassword("12345678");
        adminUser.setActive(true);
        adminUser.setCreationDate(new Date());
        adminUser.setProfilePicture("avatar.png");

        Role adminRole = roleRepository.findByRole("ADMIN");
        adminUser.setRoles(new HashSet<>(Arrays.asList(adminRole)));

        userRepository.save(adminUser);
        adminUser = userRepository.save(adminUser);
        return adminUser;
    }

    public Post createPost(UserRepository userRepository, RoleRepository roleRepository, PostRepository postRepository) {
        User user = createUser(userRepository, roleRepository);
        Post post =  new Post();
        post.setTitle("How to hack Wii U");
        post.setDescription("I am wondering whether anyone can help me hack my Wii U. My Wii U is currently running 1.5 firmware version.");
        post.setUser(user);
        post.setCreationDate(new Date());
        post.setLastModified(post.getCreationDate());
        post = postRepository.save(post);
        return post;
    }

    public PostComment createPostComment(UserRepository userRepository, RoleRepository roleRepository, PostRepository postRepository, PostCommentRepository postCommentRepository) {
        Post post = createPost(userRepository, roleRepository, postRepository);
        PostComment postComment = new PostComment();
        postComment.setContent("Visit http://www.wiiu.guide.org");
        postComment.setUser(post.getUser());
        postComment.setPostDate(new Date());
        post.addComment(postComment);
        postCommentRepository.save(postComment);
        return postComment;
    }

    public PostComment createAnotherPostCommentForTheSamePost(Post post, PostCommentRepository postCommentRepository) {
        PostComment postComment = new PostComment();
        postComment.setContent("Visit http://www.wiiu.guide.org");
        postComment.setUser(post.getUser());
        postComment.setPostDate(new Date());
        post.addComment(postComment);
        postCommentRepository.save(postComment);
        return postComment;
    }
}
