package io.codecrafts.controller;

import io.codecrafts.model.Role;
import io.codecrafts.model.User;
import io.codecrafts.repository.RoleRepository;
import io.codecrafts.service.UserService;
import io.codecrafts.util.Util;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

@Controller
public class AdminController {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserService userService;

	@GetMapping(value="/admin/users")
	public ModelAndView userAdministration(@RequestParam(defaultValue = "1")int page){
		ModelAndView modelAndView = new ModelAndView();

		List<User> users = userService.getAll();
		int totalPages = (users.size() / Util.ITEMS_PER_PAGE) + 1;
		if(users.size() % Util.ITEMS_PER_PAGE == 0) {
			totalPages -= 1;
		}

		users = userService.findAllInRange(page - 1, Util.ITEMS_PER_PAGE);
		modelAndView.addObject("users", users);
		modelAndView.addObject("totalPages", totalPages);
		modelAndView.addObject("page", page);
		modelAndView.addObject("title", "User Administration");
		modelAndView.addObject("url", "/admin/users/?page=");
		modelAndView.setViewName("admin/users");

		return modelAndView;
	}

	@GetMapping(value="/admin/users/new")
	public ModelAndView createNewUserForm(){
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.addObject("title", "New User");
		modelAndView.setViewName("admin/userform");
		return modelAndView;
	}

	@PostMapping(value = "/admin/users")
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user","There is already a user registered with the email provided");
		}

		if(user.getPassword() != null && user.getConfirmPassword() != null && !user.getPassword().equals(user.getConfirmPassword())) {
			bindingResult.rejectValue("confirmPassword", "error.user","Passwords don't match");
		}

		if (bindingResult.hasErrors()) {
			modelAndView.addObject("title", "New User");
			modelAndView.setViewName("admin/userform");
		} else {
			Role userRole = roleRepository.findByRole("USER");

			user.setActive(true);
			user.setCreationDate(new Date());
			user.setRoles(new HashSet<>(Arrays.asList(userRole)));
			userService.saveUser(user);

			modelAndView.addObject("successMessage", "User has been created successfully");
			List<User> users = userService.getAll();
			int totalPages = (users.size() / Util.ITEMS_PER_PAGE) + 1;
			int currentPage;
			if(users.size() % Util.ITEMS_PER_PAGE == 0) {
				totalPages -= 1;
				currentPage = (users.size() / Util.ITEMS_PER_PAGE) - 1;
			} else {
				currentPage = (users.size() / Util.ITEMS_PER_PAGE);
			}

			users = userService.findAllInRange(currentPage, Util.ITEMS_PER_PAGE);
			modelAndView.addObject("users", users);
			modelAndView.addObject("totalPages", totalPages);
			modelAndView.addObject("page", currentPage + 1);
			modelAndView.addObject("title", "User Administration");
			modelAndView.setViewName("admin/users");
		}
		return modelAndView;
	}

	@GetMapping(value="/admin/users/{id}/edit")
	public ModelAndView createEditForm(@PathVariable UUID id){
		ModelAndView modelAndView = new ModelAndView();
		User user = userService.findUserById(id);
		modelAndView.addObject("user", user);
		modelAndView.addObject("title", "Edit User");
		modelAndView.setViewName("admin/usereditform");
		return modelAndView;
	}

	@PostMapping(value = "/admin/users/{id}")
	public ModelAndView editUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		if (bindingResult.hasErrors()) {
			modelAndView.addObject("title", "Edit User");
			modelAndView.setViewName("admin/usereditform");
		} else {
			User updatedUser = userService.findUserById(user.getId());
			updatedUser.setFirstName(user.getFirstName());
			updatedUser.setLastName(user.getLastName());
			updatedUser.setPassword(user.getPassword());
			userService.saveUser(updatedUser);
			modelAndView.addObject("successMessage", "User has been changed successfully");
			List<User> users = userService.getAll();
			int totalPages = (users.size() / Util.ITEMS_PER_PAGE) + 1;
			if(users.size() % Util.ITEMS_PER_PAGE == 0) {
				totalPages -= 1;
			}

			users = userService.findAllInRange(0, Util.ITEMS_PER_PAGE);
			modelAndView.addObject("users", users);
			modelAndView.addObject("title", "User Administration");
			modelAndView.addObject("totalPages", totalPages);
			modelAndView.addObject("page", 1);
			modelAndView.setViewName("admin/users");
		}
		return modelAndView;
	}

	@GetMapping(value="/admin/users/{id}/delete")
	public ModelAndView deleteUser(@PathVariable UUID id){
		userService.deleteUser(id);
		ModelAndView modelAndView = new ModelAndView();
		List<User> users = userService.getAll();
		int totalPages = (users.size() / Util.ITEMS_PER_PAGE) + 1;
		if(users.size() % Util.ITEMS_PER_PAGE == 0) {
			totalPages -= 1;
		}

		users = userService.findAllInRange(0, Util.ITEMS_PER_PAGE);
		modelAndView.addObject("users", users);
		modelAndView.addObject("totalPages", totalPages);
		modelAndView.addObject("page", 1);
		modelAndView.addObject("title", "User Administration");
		modelAndView.setViewName("admin/users");
		return modelAndView;
	}

	@GetMapping(value="/admin/users/{id}/disable")
	public ModelAndView disableUser(@PathVariable UUID id){
		return setStatus(id, false);
	}

	@GetMapping(value="/admin/users/{id}/enable")
	public ModelAndView enableUser(@PathVariable UUID id){
		return setStatus(id, true);
	}

	private ModelAndView setStatus(UUID id, boolean enable) {
		User user = userService.findUserById(id);
		user.setActive(enable);
		userService.updateUser(user);
		ModelAndView modelAndView = new ModelAndView();
		List<User> users = userService.getAll();
		int totalPages = (users.size() / Util.ITEMS_PER_PAGE) + 1;
		if(users.size() % Util.ITEMS_PER_PAGE == 0) {
			totalPages -= 1;
		}

		users = userService.findAllInRange(0, Util.ITEMS_PER_PAGE);
		modelAndView.addObject("users", users);
		modelAndView.addObject("totalPages", totalPages);
		modelAndView.addObject("page", 1);
		modelAndView.addObject("title", "User Administration");
		modelAndView.setViewName("admin/users");
		return modelAndView;
	}
}
