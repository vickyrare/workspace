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

	static int ITEMS_PER_PAGE = 10;
	
	@Autowired
	private UserService userService;

	@Autowired
	private PostService postService;

	@GetMapping(value="/posts")
	public ModelAndView listPost(@RequestParam(defaultValue = "1")int page){
		ModelAndView modelAndView = new ModelAndView();
		List<Post> posts = postService.getAll();
		int totalPages = (posts.size() / ITEMS_PER_PAGE) + 1;
		if(posts.size() % ITEMS_PER_PAGE == 0) {
			totalPages -= 1;
		}
		posts = postService.findAllInRange(page - 1, ITEMS_PER_PAGE);
		modelAndView.addObject("posts", posts);
		modelAndView.addObject("totalPages", totalPages);
		modelAndView.addObject("title", "Posts");
		modelAndView.setViewName("posts");

		return modelAndView;
	}

	@GetMapping(value="/posts/new")
	public ModelAndView createNewPostForm(){
		ModelAndView modelAndView = new ModelAndView();
		Post post = new Post();
		modelAndView.addObject("post", post);
		modelAndView.addObject("title", "New Post");
		modelAndView.setViewName("postform");
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
			newPost.setLastModified(newPost.getCreationDate());
			postService.savePost(newPost);

			List<Post> posts = postService.getAll();
			int totalPages = (posts.size() / ITEMS_PER_PAGE) + 1;
			if(posts.size() % ITEMS_PER_PAGE == 0) {
				totalPages -= 1;
			}

			posts = postService.findAllInRange(0, ITEMS_PER_PAGE);
			modelAndView.addObject("posts", posts);
			modelAndView.addObject("title", "Posts");
			modelAndView.addObject("totalPages", totalPages);
			modelAndView.setViewName("posts");
		}

		return modelAndView;
	}

	@GetMapping(value="/posts/{id}/edit")
    public ModelAndView createEditPostForm(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView();
        Post post = postService.findPost(id);
        modelAndView.addObject("post", post);
		modelAndView.addObject("title", "Edit Post");
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
			editPost.setLastModified(new Date());
            postService.savePost(editPost);
			List<Post> posts = postService.getAll();
			int totalPages = (posts.size() / ITEMS_PER_PAGE) + 1;
			if(posts.size() % ITEMS_PER_PAGE == 0) {
				totalPages -= 1;
			}

			posts = postService.findAllInRange(0, ITEMS_PER_PAGE);
			modelAndView.addObject("posts", posts);
			modelAndView.addObject("title", "Posts");
			modelAndView.addObject("totalPages", totalPages);
			modelAndView.setViewName("posts");
        }
        return modelAndView;
    }

    @GetMapping(value="/posts/{id}/delete")
	public ModelAndView deletePost(@PathVariable Long id){
		postService.deletePost(id);
		ModelAndView modelAndView = new ModelAndView();
		List<Post> posts = postService.getAll();
		int totalPages = (posts.size() / ITEMS_PER_PAGE) + 1;
		if(posts.size() % ITEMS_PER_PAGE == 0) {
			totalPages -= 1;
		}

		posts = postService.findAllInRange(0, ITEMS_PER_PAGE);
		modelAndView.addObject("posts", posts);
		modelAndView.addObject("totalPages", totalPages);
		modelAndView.addObject("title", "Posts");
		modelAndView.setViewName("posts");
		return modelAndView;
	}
}
