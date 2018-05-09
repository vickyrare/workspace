package io.codecrafts.controller;

import io.codecrafts.model.Post;
import io.codecrafts.model.User;
import io.codecrafts.service.PostService;
import io.codecrafts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by waqqas on 5/8/2018.
 */

@Controller
public class SearchController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @PostMapping(value="/searchUser")
    public ModelAndView searchUserByKeyword(@RequestParam String keyword){
        ModelAndView modelAndView = new ModelAndView();
        List<User> users = userService.searchByKeyword(keyword);

        modelAndView.addObject("users", users);
        modelAndView.addObject("title", "User Administration");
        modelAndView.setViewName("/admin/users");

        return modelAndView;
    }

    @PostMapping(value="/searchPost")
    public ModelAndView searchPostByKeyword(@RequestParam String keyword){
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("user", loggedInUser);

        List<Post> posts = postService.searchByKeyword(keyword);

        modelAndView.addObject("posts", posts);
        modelAndView.addObject("title", "Posts");
        modelAndView.setViewName("posts");

        return modelAndView;
    }
}
