package io.codecrafts.controller;

import io.codecrafts.model.Role;
import io.codecrafts.model.User;
import io.codecrafts.repository.RoleRepository;
import io.codecrafts.service.UserService;
import io.codecrafts.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

@Controller
public class RegistrationController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private StorageService storageService;

	@Value("${upload.file.directory}")
	private String uploadDirectory;

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

		if(user.getPassword() != null && user.getConfirmPassword() != null && !user.getPassword().equals(user.getConfirmPassword())) {
			bindingResult.rejectValue("matchingPassword", "error.user","Passwords don't match");
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
