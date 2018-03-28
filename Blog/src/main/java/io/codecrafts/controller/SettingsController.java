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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class SettingsController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private PostService postService;

	@GetMapping(value="/settings")
	public ModelAndView settings(){
		ModelAndView modelAndView = new ModelAndView();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userService.findUserByEmail(authentication.getName());
		modelAndView.addObject("user", loggedInUser);

		modelAndView.addObject("title", "Settings");
		modelAndView.setViewName("settings");
		return modelAndView;
	}
	
	@PostMapping(value = "/settings")
	public ModelAndView updateUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userService.findUserByEmail(authentication.getName());
		modelAndView.addObject("user", loggedInUser);

		modelAndView.addObject("title", "Settings");
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("settings");
		} else {
			User updatedUser = userService.findUserByEmail(user.getEmail());
			updatedUser.setFirstName(user.getFirstName());
			updatedUser.setLastName(user.getLastName());
			updatedUser.setPassword(user.getPassword());
			userService.saveUser(updatedUser);
			modelAndView.addObject("successMessage", "User settings has been changed successfully");
			List<Post> posts = postService.getAll();
			modelAndView.addObject("posts", posts);
			modelAndView.setViewName("posts");
		}
		return modelAndView;
	}
}
