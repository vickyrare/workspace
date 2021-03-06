package io.codecrafts.controller;

import io.codecrafts.model.Post;
import io.codecrafts.model.User;
import io.codecrafts.service.PostService;
import io.codecrafts.service.UserService;
import io.codecrafts.util.Util;
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
import java.util.UUID;

@Controller
public class PostController {

	@Autowired
	private UserService userService;

	@Autowired
	private PostService postService;

	@GetMapping(value="/posts")
	public ModelAndView listPost(@RequestParam(defaultValue = "1")int page){
		ModelAndView modelAndView = new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("user", loggedInUser);

		List<Post> posts = postService.getAll();
		int totalPages = (posts.size() / Util.ITEMS_PER_PAGE) + 1;
		if(posts.size() % Util.ITEMS_PER_PAGE == 0) {
			totalPages -= 1;
		}
		posts = postService.findAllInRange(page - 1, Util.ITEMS_PER_PAGE);
		modelAndView.addObject("posts", posts);
		modelAndView.addObject("totalPages", totalPages);
		modelAndView.addObject("page", page);
		modelAndView.addObject("title", "Posts");
		modelAndView.setViewName("posts");

		return modelAndView;
	}

	@GetMapping(value="/posts/new")
	public ModelAndView createNewPostForm(){
		ModelAndView modelAndView = new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("user", loggedInUser);

		Post post = new Post();
		modelAndView.addObject("post", post);
		modelAndView.addObject("title", "New Post");
		modelAndView.setViewName("postform");
		return modelAndView;
	}

	@PostMapping(value="/posts")
	public ModelAndView addNewPost(@Valid @ModelAttribute Post post, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("user", loggedInUser);
		modelAndView.addObject("title", "Posts");

        if (bindingResult.hasErrors()) {
			modelAndView.setViewName("postform");
		} else {
			Post newPost = new Post();
			newPost.setTitle(post.getTitle());
			newPost.setDescription(post.getDescription());
			newPost.setUser(loggedInUser);
			newPost.setCreationDate(new Date());
			newPost.setLastModified(newPost.getCreationDate());
			postService.savePost(newPost);

			List<Post> posts = postService.getAll();
			int totalPages = (posts.size() / Util.ITEMS_PER_PAGE) + 1;
			if(posts.size() % Util.ITEMS_PER_PAGE == 0) {
				totalPages -= 1;
			}

			posts = postService.findAllInRange(0, Util.ITEMS_PER_PAGE);
			modelAndView.addObject("posts", posts);
			modelAndView.addObject("totalPages", totalPages);
			modelAndView.addObject("page", 1);
			modelAndView.setViewName("posts");
		}

		return modelAndView;
	}

	@GetMapping(value="/posts/{postId}/edit")
    public ModelAndView createEditPostForm(@PathVariable UUID postId){
        ModelAndView modelAndView = new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("user", loggedInUser);
		modelAndView.addObject("title", "Edit Post");

        Post post = postService.findPost(postId);

		if(loggedInUser.getId() == post.getUser().getId()) {
			modelAndView.addObject("post", post);
			modelAndView.setViewName("posteditform");
		} else {
			return getModelAndViewForAllPosts();
		}
        return modelAndView;
	}

    @PostMapping(value="/posts/{id}")
    public ModelAndView editPost(@Valid @ModelAttribute Post post, BindingResult bindingResult){
        ModelAndView modelAndView = new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("user", loggedInUser);

        if (bindingResult.hasErrors()) {
			modelAndView.addObject("title", "Edit Post");
            modelAndView.setViewName("posteditform");
        } else {
			Post editPost = postService.findPost(post.getId());
			if(loggedInUser.getId() == editPost.getUser().getId()) {
				editPost.setTitle(post.getTitle());
				editPost.setDescription(post.getDescription());
				editPost.setLastModified(new Date());
				postService.savePost(editPost);
			}

			List<Post> posts = postService.getAll();
			int totalPages = (posts.size() / Util.ITEMS_PER_PAGE) + 1;
			if (posts.size() % Util.ITEMS_PER_PAGE == 0) {
				totalPages -= 1;
			}
			posts = postService.findAllInRange(0, Util.ITEMS_PER_PAGE);

			modelAndView.addObject("posts", posts);
			modelAndView.addObject("title", "Posts");
			modelAndView.addObject("totalPages", totalPages);
			modelAndView.addObject("page", 1);
			modelAndView.setViewName("posts");
        }
        return modelAndView;
    }

    @GetMapping(value="/posts/{postId}/delete")
	public ModelAndView deletePost(@PathVariable UUID postId){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userService.findUserByEmail(auth.getName());
		Post post = postService.findPost(postId);

		if(loggedInUser.isAdmin() || loggedInUser.getId() == post.getUser().getId()) {
			postService.deletePost(postId);
		}

		return getModelAndViewForAllPosts();
	}

	private ModelAndView getModelAndViewForAllPosts() {
		ModelAndView modelAndView = new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("user", loggedInUser);

		List<Post> posts = postService.getAll();
		int totalPages = (posts.size() / Util.ITEMS_PER_PAGE) + 1;
		if(posts.size() % Util.ITEMS_PER_PAGE == 0) {
			totalPages -= 1;
		}

		posts = postService.findAllInRange(0, Util.ITEMS_PER_PAGE);
		modelAndView.addObject("posts", posts);
		modelAndView.addObject("totalPages", totalPages);
		modelAndView.addObject("page", 1);
		modelAndView.addObject("title", "Posts");
		modelAndView.setViewName("posts");
		return modelAndView;
	}
}
