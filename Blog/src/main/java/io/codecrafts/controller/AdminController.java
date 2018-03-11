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
import java.util.List;

@Controller
public class AdminController {

	static int ITEMS_PER_PAGE = 4;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value="/admin/users", method = RequestMethod.GET)
	public ModelAndView userAdministration(@RequestParam(defaultValue = "1")int page){
		ModelAndView modelAndView = new ModelAndView();

		List<User> users = userService.getAll();
		int totalPages = (users.size() / ITEMS_PER_PAGE) + 1;
		if(users.size() % ITEMS_PER_PAGE == 0) {
			totalPages -= 1;
		}

		users = userService.findAllInRange(page - 1, ITEMS_PER_PAGE);
		modelAndView.addObject("users", users);
		modelAndView.addObject("totalPages", totalPages);
		modelAndView.addObject("title", "User Administration");
		modelAndView.setViewName("/admin/users");

		return modelAndView;
	}

	@GetMapping(value="/admin/users/new")
	public ModelAndView createNewUserForm(){
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.addObject("title", "New User");
		modelAndView.setViewName("/admin/userform");
		return modelAndView;
	}

	@RequestMapping(value = "/admin/users", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult
					.rejectValue("email", "error.user",
								 "There is already a user registered with the email provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("/admin/userform");
		} else {
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User has been created successfully");
			List<User> users = userService.getAll();
			int totalPages = (users.size() / ITEMS_PER_PAGE) + 1;
			int currentPage;
			if(users.size() % ITEMS_PER_PAGE == 0) {
				totalPages -= 1;
				currentPage = (users.size() / ITEMS_PER_PAGE) - 1;
			} else {
				currentPage = (users.size() / ITEMS_PER_PAGE);
			}

			users = userService.findAllInRange(currentPage, ITEMS_PER_PAGE);
			modelAndView.addObject("users", users);
			modelAndView.addObject("totalPages", totalPages);
			modelAndView.addObject("title", "User Administration");
			modelAndView.setViewName("/admin/users");
		}
		return modelAndView;
	}

	@RequestMapping(value="/admin/users/{id}/edit", method = RequestMethod.GET)
	public ModelAndView createEditForm(@PathVariable Long id){
		ModelAndView modelAndView = new ModelAndView();
		User user = userService.findUserById(id);
		modelAndView.addObject("user", user);
		modelAndView.setViewName("/admin/usereditform");
		return modelAndView;
	}

	@RequestMapping(value = "/admin/users/{id}", method = RequestMethod.POST)
	public ModelAndView editUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("/admin/userform");
		} else {
			User updatedUser = userService.findUserById(user.getId());
			updatedUser.setFirstName(user.getFirstName());
			updatedUser.setLastName(user.getLastName());
			updatedUser.setPassword(user.getPassword());
			userService.saveUser(updatedUser);
			modelAndView.addObject("successMessage", "User has been changed successfully");
			List<User> users = userService.getAll();
			int totalPages = (users.size() / ITEMS_PER_PAGE) + 1;
			if(users.size() % ITEMS_PER_PAGE == 0) {
				totalPages -= 1;
			}

			users = userService.findAllInRange(0, ITEMS_PER_PAGE);
			modelAndView.addObject("users", users);
			modelAndView.addObject("title", "User Administration");
			modelAndView.addObject("totalPages", totalPages);
			modelAndView.setViewName("/admin/users");
		}
		return modelAndView;
	}

	@GetMapping(value="/admin/users/{id}/delete")
	public ModelAndView deletePost(@PathVariable Long id){
		userService.deleteUser(id);
		ModelAndView modelAndView = new ModelAndView();
		List<User> users = userService.getAll();
		int totalPages = (users.size() / ITEMS_PER_PAGE) + 1;
		if(users.size() % ITEMS_PER_PAGE == 0) {
			totalPages -= 1;
		}

		users = userService.findAllInRange(0, ITEMS_PER_PAGE);
		modelAndView.addObject("users", users);
		modelAndView.addObject("totalPages", totalPages);
		modelAndView.addObject("title", "User Administration");
		modelAndView.setViewName("/admin/users");
		return modelAndView;
	}
}
