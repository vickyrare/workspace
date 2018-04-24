package io.codecrafts.rest;

import io.codecrafts.model.Post;
import io.codecrafts.model.Role;
import io.codecrafts.model.User;
import io.codecrafts.repository.RoleRepository;
import io.codecrafts.rest.annotations.RestApiController;
import io.codecrafts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

/**
 * Created by waqqas on 4/22/2018.
 */
@RestApiController
public class UserRestController {

    static int ITEMS_PER_PAGE = 10;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping(value="/users")
    public List<User> findAll(@RequestParam(defaultValue = "1")int page){
        List<User> users = userService.findAllInRange(page - 1, ITEMS_PER_PAGE);
        return users;
    }

    @PostMapping(value="/users")
    public User registration(@RequestBody User user){
        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setActive(true);
        newUser.setCreationDate(new Date());
        newUser.setProfilePicture(user.getProfilePicture());
        Role userRole = roleRepository.findByRole("USER");
        newUser.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userService.saveUser(newUser);
    }

    @GetMapping(value="/users/{userId}")
    public User findUser(@PathVariable UUID userId){
        User user = userService.findUserById(userId);
        return user;
    }

    @PutMapping(value="/users/{userId}")
    public void editUser(@RequestBody User user, @PathVariable UUID userId) {
        User updatedUser = userService.findUserById(userId);
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setProfilePicture(user.getProfilePicture());
        userService.saveUser(updatedUser);
    }

    @DeleteMapping(value="/users/{userId}")
    public void deleteUser(@PathVariable UUID userId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.findUserByEmail(auth.getName());

        if(loggedInUser == null || loggedInUser.isAdmin()) {
            userService.deleteUser(userId);
        }
    }
}
