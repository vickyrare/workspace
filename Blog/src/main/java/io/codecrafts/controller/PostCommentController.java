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
	public ModelAndView getAllComments(@PathVariable Long postId, @RequestParam(defaultValue = "1")int page){
		ModelAndView modelAndView = new ModelAndView();

		List<PostComment> postComments = postCommentService.getAllComments(postId);
		int totalPages = (postComments.size() / ITEMS_PER_PAGE) + 1;
		if(postComments.size() % ITEMS_PER_PAGE == 0) {
			totalPages -= 1;
		}

		postComments = postCommentService.findAllInRange(postId, page - 1, ITEMS_PER_PAGE);
		Post post = postService.findPost(postId);
		modelAndView.addObject("post", post);
		modelAndView.addObject("comments", postComments);
		modelAndView.addObject("totalPages", totalPages);
		modelAndView.addObject("title", "Comments");
		modelAndView.setViewName("comments");
		return modelAndView;
	}

	@GetMapping(value="/posts/{postId}/comments/new")
	public ModelAndView newCommentForm(@PathVariable Long postId){
		ModelAndView modelAndView = new ModelAndView();
		PostComment postComment = new PostComment();
		modelAndView.addObject("postid", postId);
		modelAndView.addObject("postComment", postComment);
		modelAndView.addObject("title", "New Comment");
		modelAndView.setViewName("commentform");
		return modelAndView;
	}

	@PostMapping(value="/posts/{postId}/comments")
	public ModelAndView addNewComment(@Valid @ModelAttribute PostComment postComment, BindingResult bindingResult, @PathVariable Long postId){
		ModelAndView modelAndView = new ModelAndView();

		if (bindingResult.hasErrors()) {
			modelAndView.addObject("postid", postId);
			modelAndView.setViewName("commentform");
		} else {
			PostComment newPostComment = new PostComment();
			newPostComment.setContent(postComment.getContent());
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			Post post = postService.findPost(postId);
			User loggedInUser = userService.findUserByEmail(authentication.getName());
			newPostComment.setUser(loggedInUser);
			newPostComment.setPostDate(new Date());
			post.addComment(newPostComment);
			postService.savePost(post);
			List<PostComment> postComments = postCommentService.getAllComments(postId);

			int totalPages = (postComments.size() / ITEMS_PER_PAGE) + 1;
			int currentPage;
			if(postComments.size() % ITEMS_PER_PAGE == 0) {
				totalPages -= 1;
				currentPage = (postComments.size() / ITEMS_PER_PAGE) - 1;
			} else {
				currentPage = (postComments.size() / ITEMS_PER_PAGE);
			}

			postComments = postCommentService.findAllInRange(postId, currentPage, ITEMS_PER_PAGE);

			modelAndView.addObject("post", post);
			modelAndView.addObject("comments", postComments);
			modelAndView.addObject("totalPages", totalPages);
			modelAndView.addObject("title", "Comments");
			modelAndView.setViewName("comments");
		}
		return modelAndView;
	}

	@GetMapping(value="/posts/{postId}/comments/{commentId}/edit")
    public ModelAndView editCommentForm(@PathVariable Long postId, @PathVariable Long commentId){
        ModelAndView modelAndView = new ModelAndView();
        PostComment postComment = postCommentService.findPostComment(commentId);
        modelAndView.addObject("postid", postId);
		modelAndView.addObject("postComment", postComment);
		modelAndView.addObject("title", "Edit Comment");
        modelAndView.setViewName("commenteditform");
        return modelAndView;
	}

    @PostMapping(value="/posts/{postId}/comments/{commentId}")
    public ModelAndView editComment(@Valid @ModelAttribute PostComment postComment, BindingResult bindingResult, @PathVariable Long postId, @PathVariable Long commentId) {
		ModelAndView modelAndView = new ModelAndView();

		if (bindingResult.hasErrors()) {
			postComment.setId(commentId);
			modelAndView.addObject("postid", postId);
			modelAndView.addObject("comment", postComment);

			modelAndView.setViewName("commenteditform");
		} else {
			PostComment editPostComment = postCommentService.findPostComment(commentId);
			editPostComment.setContent(postComment.getContent());
			postCommentService.updateComment(editPostComment);
			List<PostComment> postComments = postCommentService.getAllComments(postId);

			int totalPages = (postComments.size() / ITEMS_PER_PAGE) + 1;
			if(postComments.size() % ITEMS_PER_PAGE == 0) {
				totalPages -= 1;
			}

			postComments = postCommentService.findAllInRange(postId, 0, ITEMS_PER_PAGE);
			Post post = postService.findPost(postId);
			modelAndView.addObject("post", post);
			modelAndView.addObject("comments", postComments);
			modelAndView.addObject("totalPages", totalPages);
			modelAndView.addObject("title", "Comments");
			modelAndView.setViewName("comments");
		}
		return modelAndView;
	}

	@GetMapping(value="/posts/{postId}/comments/{commentId}/delete")
	public ModelAndView deleteComment(@PathVariable Long postId, @PathVariable Long commentId){
		postCommentService.deleteComment(commentId);
		ModelAndView modelAndView = new ModelAndView();
		List<PostComment> postComments = postCommentService.getAllComments(postId);
		int totalPages = (postComments.size() / ITEMS_PER_PAGE) + 1;
		if(postComments.size() % ITEMS_PER_PAGE == 0) {
			totalPages -= 1;
		}

		postComments = postCommentService.findAllInRange(postId, 0, ITEMS_PER_PAGE);
		Post post = postService.findPost(postId);
		modelAndView.addObject("post", post);
		modelAndView.addObject("comments", postComments);
		modelAndView.addObject("totalPages", totalPages);
		modelAndView.addObject("title", "Comments");
		modelAndView.setViewName("comments");
		return modelAndView;
	}
}