package io.codecrafts;

import io.codecrafts.model.Post;
import io.codecrafts.model.PostComment;
import io.codecrafts.model.Role;
import io.codecrafts.model.User;
import io.codecrafts.repository.PostRepository;
import io.codecrafts.repository.RoleRepository;
import io.codecrafts.service.UserService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

@Component
public class CustomCommandLineRunner implements CommandLineRunner {

    //don't add @Autowired as it causes circular dependency on Mac OS and Linux
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PostRepository postRepository;

    @Value("${upload.file.directory}")
    private String uploadDirectory;

    public CustomCommandLineRunner(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        /*User adminUser = new User();
        adminUser.setFirstName("Waqqas");
        adminUser.setLastName("Sharif");
        adminUser.setEmail("vickyrare@gmail.com");
        adminUser.setPassword("12345678");
        adminUser.setActive(true);
        adminUser.setCreationDate(new Date());
        adminUser.setProfilePicture("avatar.png");

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
        user.setProfilePicture("avatar.png");

        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

        userService.saveUser(user);
        
        Post post = new Post();
        post.setTitle("How to hack Wii U");
        post.setDescription("I am wondering whether anyone can help me hack my Wii U. My Wii U is currently running 1.5 firmware version.");
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
        */
        String adminUserFolder = uploadDirectory + "/79a5bd3f-1ec2-46cf-94b6-8ac23df3f3c9";
        String userFolder = uploadDirectory + "/f9d98297-9db9-41a3-86e6-25ab0480fcd8";

        Resource resource = new ClassPathResource("/static/images/avatar.png");

        InputStream initialStream = resource.getInputStream();
        File targetFile = new File("src/main/resources/targetFile.png");
        OutputStream outStream = new FileOutputStream(targetFile);

        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        while ((bytesRead = initialStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        IOUtils.closeQuietly(initialStream);
        IOUtils.closeQuietly(outStream);

        if (!Files.exists(Paths.get(adminUserFolder))) {
            Files.createDirectory(Paths.get(adminUserFolder));
            Files.copy(Paths.get(targetFile.getAbsolutePath()), Paths.get(adminUserFolder + "/avatar.png"));
        }

        if (!Files.exists(Paths.get(userFolder))) {
            Files.createDirectory(Paths.get(userFolder));
            Files.copy(Paths.get(targetFile.getAbsolutePath()), Paths.get(userFolder + "/avatar.png"));
        }
    }
}
