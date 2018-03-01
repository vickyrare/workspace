package io.codecrafts.controller;

import io.codecrafts.model.Post;
import io.codecrafts.model.User;
import io.codecrafts.service.PostService;
import io.codecrafts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
public class PostController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private PostService postService;

	@GetMapping(value="/user/post")
	public ModelAndView listPost(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("user/post");
		return modelAndView;
	}

	@GetMapping(value="/user/post/new")
	public ModelAndView createNewPostForm(){
		ModelAndView modelAndView = new ModelAndView();
		Post post = new Post();
		modelAndView.addObject("post", post);
		modelAndView.setViewName("user/post/postform");
		return modelAndView;
	}

	@PostMapping(value="/user/post")
	public ModelAndView addNewPost(@Valid @ModelAttribute Post post, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();

        Post newPost = new Post();
		newPost.setTitle(post.getTitle());

		if (post.getTitle() == null) {
			bindingResult.rejectValue("title", "error.title","Please provide title.");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("user/post/postform");
		} else {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			User loggedInUser = userService.findUserByEmail(authentication.getName());
			newPost.setUser(loggedInUser);
			newPost.setCreationDate(new Date());
			postService.savePost(newPost);

			List<Post> posts = postService.getAll();
			modelAndView.addObject("posts", posts);
			modelAndView.setViewName("user/home");
		}

		return modelAndView;
	}

    @GetMapping(value="/user/post/{id}/edit")
    public ModelAndView createEditPostForm(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView();
        Post post = postService.findPost(id);
        modelAndView.addObject("post", post);
        modelAndView.setViewName("user/post/posteditform");
        return modelAndView;
	}

    @PostMapping(value="/user/post/edit")
    public ModelAndView editPost(@Valid @ModelAttribute Post post, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();

        if (post.getTitle() == null) {
            bindingResult.rejectValue("title", "error.title","Please provide title.");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("user/post/posteditform");
        } else {
            Post editPost = postService.findPost(post.getId());
            editPost.setTitle(post.getTitle());
            postService.savePost(editPost);
            List<Post> posts = postService.getAll();
            modelAndView.addObject("posts", posts);
            modelAndView.setViewName("user/home");
        }
        return modelAndView;
    }

    @GetMapping(value="/user/post/{id}/delete")
	public ModelAndView deletePost(@PathVariable Long id){
		postService.deletePost(id);
		ModelAndView modelAndView = new ModelAndView();
		List<Post> posts = postService.getAll();
		modelAndView.addObject("posts", posts);
		modelAndView.setViewName("user/home");
		return modelAndView;
	}
}
