package io.codecrafts.controller;

import javax.validation.Valid;

import io.codecrafts.model.Post;
import io.codecrafts.model.Role;
import io.codecrafts.model.User;
import io.codecrafts.repository.RoleRepository;
import io.codecrafts.service.PostService;
import io.codecrafts.service.UserService;
import io.codecrafts.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private StorageService storageService;

	@Value("${upload.file.directory}")
	private String uploadDirectory;

	@GetMapping(value={"/", "/login"})
	public ModelAndView login(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("title", "Login");
		modelAndView.setViewName("login");
		return modelAndView;
	}
	
	
	@GetMapping(value="/registration")
	public ModelAndView registration(){
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.addObject("title", "Registration");
		modelAndView.setViewName("registration");
		return modelAndView;
	}
	
	@PostMapping(value = "/registration")
	public ModelAndView createNewUser(@Valid @ModelAttribute User user, BindingResult bindingResult , @RequestParam("file") MultipartFile file) throws IOException {
		Path uploadDirectoryPath = Paths.get(uploadDirectory);
		if (!Files.exists(uploadDirectoryPath)) {
			Files.createDirectory(uploadDirectoryPath);
		}

		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user","There is already a user registered with the email provided");
		}

		if (file == null || file.isEmpty()) {
			bindingResult.rejectValue("profilePicture", "error.user","Please provide a profile picture");
		}

		if (bindingResult.hasErrors()) {
			modelAndView.addObject("title", "Registration");
			modelAndView.setViewName("registration");
		} else {
			user.setActive(true);
			storageService.store(file);
			user.setCreationDate(new Date());
			user.setProfilePicture(file.getOriginalFilename());
			Role userRole = roleRepository.findByRole("USER");
			user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
			userService.saveUser(user);
			File dir = new File(uploadDirectoryPath.toFile(), user.getId().toString());
			if(!dir.exists()) {
				dir.mkdirs();
			}
			file.transferTo(new File(dir, file.getOriginalFilename()));
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.addObject("title", "Registration");
			modelAndView.setViewName("registration");
		}
		return modelAndView;
	}
}
