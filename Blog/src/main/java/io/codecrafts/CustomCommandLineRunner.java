package io.codecrafts;

import io.codecrafts.model.Post;
import io.codecrafts.model.PostComment;
import io.codecrafts.model.Role;
import io.codecrafts.model.User;
import io.codecrafts.repository.PostRepository;
import io.codecrafts.repository.RoleRepository;
import io.codecrafts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

@Component
public class CustomCommandLineRunner implements CommandLineRunner {

    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PostRepository postRepository;

    public CustomCommandLineRunner(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        User adminUser = new User();
        adminUser.setFirstName("Waqqas");
        adminUser.setLastName("Sharif");
        adminUser.setEmail("vickyrare@gmail.com");
        adminUser.setPassword("12345678");
        adminUser.setActive(true);
        adminUser.setCreationDate(new Date());

        Role adminRole = roleRepository.findByRole("ADMIN");
        adminUser.setRoles(new HashSet<Role>(Arrays.asList(adminRole)));

        userService.saveUser(adminUser);

        User user = new User();
        user.setFirstName("Waqqas");
        user.setLastName("Sharif");
        user.setEmail("vickyrare@yahoo.com");
        user.setPassword("12345678");
        user.setActive(true);
        user.setCreationDate(new Date());

        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

        userService.saveUser(user);
        
        Post post =  new Post();
        post.setTitle("How to hack Wii U");
        post.setUser(user);
        post.setCreationDate(new Date());
        post.setLastModified(post.getCreationDate());
        postRepository.save(post);

        PostComment postComment = new PostComment();
        postComment.setContent("Visit http://www.wiiu.guide.org");
        postComment.setUser(user);
        postComment.setPostDate(new Date());
        post.addComment(postComment);
        postRepository.save(post);
    }
}
