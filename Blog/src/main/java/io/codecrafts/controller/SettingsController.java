package io.codecrafts.controller;

import io.codecrafts.model.Post;
import io.codecrafts.model.User;
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

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

	@Autowired
	private StorageService storageService;

	@Value("${upload.file.directory}")
	private String uploadDirectory;
	
	@PostMapping(value = "/settings")
	public ModelAndView updateUser(@Valid User user, BindingResult bindingResult, @RequestParam("file") MultipartFile file) throws IOException {
		Path uploadDirectoryPath = Paths.get(uploadDirectory);

		ModelAndView modelAndView = new ModelAndView();

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User loggedInUser = userService.findUserByEmail(authentication.getName());
		modelAndView.addObject("user", loggedInUser);

		if (bindingResult.hasErrors()) {
			modelAndView.addObject("title", "Settings");
			modelAndView.setViewName("settings");
		} else {
			User updatedUser = null;
			if(user.getEmail().equals(loggedInUser.getEmail())) {
				updatedUser = userService.findUserByEmail(user.getEmail());
				updatedUser.setFirstName(user.getFirstName());
				updatedUser.setLastName(user.getLastName());
				updatedUser.setPassword(user.getPassword());
				storageService.store(file);
				String previousProfilePictureName = updatedUser.getProfilePicture();
				updatedUser.setProfilePicture(file.getOriginalFilename());
				userService.saveUser(updatedUser);
				File dir = new File(uploadDirectoryPath.toFile(), updatedUser.getId().toString());
				modelAndView.addObject("user", updatedUser);

				//remove previous profile picture
				File previousProfilePicture = new File(dir, previousProfilePictureName);
				if (previousProfilePicture.exists()) {
					previousProfilePicture.delete();
				}

				file.transferTo(new File(dir, file.getOriginalFilename()));
				modelAndView.addObject("successMessage", "User settings has been changed successfully");
			}

			List<Post> posts = postService.getAll();
			modelAndView.addObject("posts", posts);
			modelAndView.addObject("title", "Posts");
			modelAndView.setViewName("posts");
		}
		return modelAndView;
	}
}
