package io.codecrafts.controller;

import io.codecrafts.model.Post;
import io.codecrafts.model.User;
import io.codecrafts.service.PostService;
import io.codecrafts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    static int ITEMS_PER_PAGE = 10;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @PostMapping(value="/searchUser")
    public ModelAndView searchUserByKeyword(@RequestParam String keyword, @RequestParam(defaultValue = "1")int page){
        return searchUser(keyword, page);
    }

    @GetMapping(value="/searchUser")
    public ModelAndView searchUserByKeywordUsingGet(@RequestParam String keyword, @RequestParam(defaultValue = "1")int page){
         return searchUser(keyword, page);
    }

    private ModelAndView searchUser(String keyword, int page) {
        ModelAndView modelAndView = new ModelAndView();

        List<User> users = null;
        if(keyword == null || keyword.equals("")) {
            users = userService.getAll();
        } else {
            users = userService.searchByKeyword(keyword, null);
        }

        int totalPages = (users.size() / ITEMS_PER_PAGE) + 1;
        if(users.size() % ITEMS_PER_PAGE == 0) {
            totalPages -= 1;
        }

        if(keyword == null || keyword.equals("")) {
            users = userService.findAllInRange(page - 1, ITEMS_PER_PAGE);
        } else {
            users = userService.searchByKeyword(keyword, new PageRequest(page - 1, ITEMS_PER_PAGE));
        }

        modelAndView.addObject("users", users);
        modelAndView.addObject("totalPages", totalPages);
        modelAndView.addObject("page", page);
        modelAndView.addObject("keyword", keyword);
        modelAndView.addObject("title", "User Administration");
        modelAndView.addObject("url", "/searchUser/?keyword=" + keyword + "&page=");
        modelAndView.setViewName("/admin/users");

        return modelAndView;
    }

    @PostMapping(value="/searchPost")
    public ModelAndView searchPostByKeyword(@RequestParam String keyword, @RequestParam(defaultValue = "1")int page){
        return searchPost(keyword, page);
    }

    @GetMapping(value="/searchPost")
    public ModelAndView searchPostByKeywordByGet(@RequestParam String keyword, @RequestParam(defaultValue = "1")int page){
        return searchPost(keyword, page);
    }

    private ModelAndView searchPost(String keyword, int page) {
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("user", loggedInUser);

        List<Post> posts = null;
        if(keyword == null || keyword.equals("")) {
            posts = postService.getAll();
        } else {
            posts = postService.searchByKeyword(keyword, null);
        }

        int totalPages = (posts.size() / ITEMS_PER_PAGE) + 1;
        if(posts.size() % ITEMS_PER_PAGE == 0) {
            totalPages -= 1;
        }

        if(keyword == null || keyword.equals("")) {
            posts = postService.findAllInRange(page - 1, ITEMS_PER_PAGE);
        } else {
            posts = postService.searchByKeyword(keyword, new PageRequest(page - 1, ITEMS_PER_PAGE));
        }

        modelAndView.addObject("posts", posts);
        modelAndView.addObject("totalPages", totalPages);
        modelAndView.addObject("page", page);
        modelAndView.addObject("keyword", keyword);
        modelAndView.addObject("title", "Posts");
        modelAndView.addObject("url", "/searchPost/?keyword=" + keyword + "&page=");
        modelAndView.setViewName("posts");

        return modelAndView;
    }
}
