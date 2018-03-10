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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
		modelAndView.setViewName("admin/users");

		return modelAndView;
	}
}
