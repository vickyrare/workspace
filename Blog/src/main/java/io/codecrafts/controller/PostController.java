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

	@GetMapping(value="/posts")
	public ModelAndView listPost(){
		ModelAndView modelAndView = new ModelAndView();
		List<Post> posts = postService.getAll();
		modelAndView.addObject("posts", posts);
		modelAndView.setViewName("posts");
		return modelAndView;
	}

	@GetMapping(value="/posts/new")
	public ModelAndView createNewPostForm(){
		ModelAndView modelAndView = new ModelAndView();
		Post post = new Post();
		modelAndView.addObject("post", post);
		modelAndView.setViewName("/postform");
		return modelAndView;
	}

	@PostMapping(value="/posts")
	public ModelAndView addNewPost(@Valid @ModelAttribute Post post, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
			modelAndView.setViewName("postform");
		} else {
			Post newPost = new Post();
			newPost.setTitle(post.getTitle());
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			User loggedInUser = userService.findUserByEmail(authentication.getName());
			newPost.setUser(loggedInUser);
			newPost.setCreationDate(new Date());
			postService.savePost(newPost);

			List<Post> posts = postService.getAll();
			modelAndView.addObject("posts", posts);
			modelAndView.setViewName("posts");
		}

		return modelAndView;
	}

	@GetMapping(value="/posts/{id}/edit")
    public ModelAndView createEditPostForm(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView();
        Post post = postService.findPost(id);
        modelAndView.addObject("post", post);
        modelAndView.setViewName("posteditform");
        return modelAndView;
	}

    @PostMapping(value="/posts/{id}")
    public ModelAndView editPost(@Valid @ModelAttribute Post post, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("posteditform");
        } else {
            Post editPost = postService.findPost(post.getId());
            editPost.setTitle(post.getTitle());
            postService.savePost(editPost);
            List<Post> posts = postService.getAll();
            modelAndView.addObject("posts", posts);
            modelAndView.setViewName("posts");
        }
        return modelAndView;
    }

    @GetMapping(value="/posts/{id}/delete")
	public ModelAndView deletePost(@PathVariable Long id){
		postService.deletePost(id);
		ModelAndView modelAndView = new ModelAndView();
		List<Post> posts = postService.getAll();
		modelAndView.addObject("posts", posts);
		modelAndView.setViewName("posts");
		return modelAndView;
	}
}
