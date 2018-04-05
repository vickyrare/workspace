package io.codecrafts.controller;

import io.codecrafts.model.Post;
import io.codecrafts.model.PostComment;
import io.codecrafts.model.User;
import io.codecrafts.service.PostCommentService;
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
import java.util.UUID;

@Controller
public class PostCommentController {

	static int ITEMS_PER_PAGE = 10;
	
	@Autowired
	private UserService userService;

	@Autowired
	private PostService postService;

	@Autowired
	private PostCommentService postCommentService;

	@GetMapping(value="/posts/{postId}/comments")
	public ModelAndView getAllComments(@PathVariable UUID postId, @RequestParam(defaultValue = "1")int page){
		ModelAndView modelAndView = new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("user", loggedInUser);

		List<PostComment> postComments = postCommentService.getAllComments(postId);
		int totalPages = (postComments.size() / ITEMS_PER_PAGE) + 1;
		if(postComments.size() % ITEMS_PER_PAGE == 0) {
			totalPages -= 1;
		}

		postComments = postCommentService.findAllInRange(postId, page - 1, ITEMS_PER_PAGE);
		Post post = postService.findPost(postId);
		modelAndView.addObject("post", post);
		PostComment postComment = new PostComment();
		modelAndView.addObject("postComment", postComment);
		modelAndView.addObject("comments", postComments);
		modelAndView.addObject("totalPages", totalPages);
		modelAndView.addObject("page", page);
		modelAndView.addObject("title", "Comments");
		modelAndView.setViewName("comments");
		return modelAndView;
	}

	@PostMapping(value="/posts/{postId}/comments")
	public ModelAndView addNewComment(@Valid @ModelAttribute PostComment postComment, BindingResult bindingResult, @PathVariable UUID postId){
		ModelAndView modelAndView = new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("user", loggedInUser);

		Post post = postService.findPost(postId);

		if (!bindingResult.hasErrors()) {
			PostComment newPostComment = new PostComment();
			newPostComment.setContent(postComment.getContent());
			newPostComment.setUser(loggedInUser);
			newPostComment.setPostDate(new Date());
			post.addComment(newPostComment);
			post.setLastModified(newPostComment.getPostDate());
			postService.savePost(post);
			postComment = new PostComment();
			modelAndView.addObject("postComment", postComment);
		}
		List<PostComment> postComments = postCommentService.getAllComments(postId);

		int totalPages = (postComments.size() / ITEMS_PER_PAGE) + 1;
		if(postComments.size() % ITEMS_PER_PAGE == 0) {
			totalPages -= 1;
		}

		postComments = postCommentService.findAllInRange(postId, 0, ITEMS_PER_PAGE);

		modelAndView.addObject("post", post);
		modelAndView.addObject("comments", postComments);
		modelAndView.addObject("totalPages", totalPages);
		modelAndView.addObject("page", 1);
		modelAndView.addObject("title", "Comments");
		modelAndView.setViewName("comments");
		return modelAndView;
	}

	@GetMapping(value="/posts/{postId}/comments/{commentId}/edit")
    public ModelAndView editCommentForm(@PathVariable UUID postId, @PathVariable UUID commentId){
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("user", loggedInUser);
		modelAndView.addObject("title", "Edit Comment");

		PostComment postComment = postCommentService.findPostComment(commentId);
		if(loggedInUser.getId() == postComment.getUser().getId()) {
			Post post = postService.findPost(postId);
			modelAndView.addObject("post", post);
			postComment = postCommentService.findPostComment(commentId);
			modelAndView.addObject("postComment", postComment);
			modelAndView.setViewName("commenteditform");
		} else {
			return getModelAndViewForPost(postId);
		}
        return modelAndView;
	}

    @PostMapping(value="/posts/{postId}/comments/{commentId}")
    public ModelAndView editComment(@Valid @ModelAttribute PostComment postComment, BindingResult bindingResult, @PathVariable UUID postId, @PathVariable UUID commentId) {
		ModelAndView modelAndView = new ModelAndView();
		PostComment editPostComment = postCommentService.findPostComment(commentId);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("user", loggedInUser);

		if(loggedInUser.getId() == editPostComment.getUser().getId()) {
			if (bindingResult.hasErrors()) {
				postComment.setId(commentId);
				modelAndView.addObject("postid", postId);
				modelAndView.addObject("comment", postComment);
				modelAndView.addObject("title", "Edit Comment");
				modelAndView.setViewName("commenteditform");
			} else {
				editPostComment.setContent(postComment.getContent());
				postCommentService.updateComment(editPostComment);
				List<PostComment> postComments = postCommentService.getAllComments(postId);

				int totalPages = (postComments.size() / ITEMS_PER_PAGE) + 1;
				if (postComments.size() % ITEMS_PER_PAGE == 0) {
					totalPages -= 1;
				}

				postComments = postCommentService.findAllInRange(postId, 0, ITEMS_PER_PAGE);
				Post post = postService.findPost(postId);
				post.setLastModified(editPostComment.getPostDate());
				postService.savePost(post);
				modelAndView.addObject("post", post);
				postComment = new PostComment();
				modelAndView.addObject("postComment", postComment);
				modelAndView.addObject("comments", postComments);
				modelAndView.addObject("totalPages", totalPages);
				modelAndView.addObject("page", 1);
				modelAndView.addObject("title", "Comments");
				modelAndView.setViewName("comments");
			}
		} else {
			return getModelAndViewForPost(postId);
		}
		return modelAndView;
	}

	@GetMapping(value="/posts/{postId}/comments/{commentId}/delete")
	public ModelAndView deleteComment(@PathVariable UUID postId, @PathVariable UUID commentId){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userService.findUserByEmail(auth.getName());

		PostComment postComment = postCommentService.findPostComment(commentId);
    	if(loggedInUser.isAdmin() || loggedInUser.getId() == postComment.getUser().getId()) {
			postCommentService.deleteComment(commentId);
		}
		return getModelAndViewForPost(postId);
	}

	private ModelAndView getModelAndViewForPost(UUID postId) {
		ModelAndView modelAndView = new ModelAndView();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("user", loggedInUser);
		List<PostComment> postComments = postCommentService.getAllComments(postId);

		int totalPages = (postComments.size() / ITEMS_PER_PAGE) + 1;
		if(postComments.size() % ITEMS_PER_PAGE == 0) {
			totalPages -= 1;
		}

		postComments = postCommentService.findAllInRange(postId, 0, ITEMS_PER_PAGE);
		Post post = postService.findPost(postId);
		modelAndView.addObject("post", post);
		PostComment postComment = new PostComment();
		modelAndView.addObject("postComment", postComment);
		modelAndView.addObject("comments", postComments);
		modelAndView.addObject("totalPages", totalPages);
		modelAndView.addObject("page", 1);
		modelAndView.addObject("title", "Comments");
		modelAndView.setViewName("comments");
		return modelAndView;
	}
}
