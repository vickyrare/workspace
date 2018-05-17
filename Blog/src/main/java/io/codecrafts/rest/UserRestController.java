package io.codecrafts.rest;

import io.codecrafts.error.CustomErrorType;
import io.codecrafts.model.Role;
import io.codecrafts.model.User;
import io.codecrafts.repository.RoleRepository;
import io.codecrafts.rest.annotations.RestApiController;
import io.codecrafts.service.UserService;
import io.codecrafts.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

/**
 * Created by waqqas on 4/22/2018.
 */
@RestApiController
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> listAllUsers(@RequestParam(defaultValue = "1")int page) {
        List<User> users = userService.findAllInRange(page - 1, Util.ITEMS_PER_PAGE);
        if (users.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
        if (userService.findUserByEmail(user.getEmail()) != null) {
            return new ResponseEntity(new CustomErrorType("Unable to create. A User with email " + user.getEmail() + " already exist."),HttpStatus.CONFLICT);
        }

        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));

        userService.saveUser(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/users/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    @GetMapping(value="/users/{userId}")
    public ResponseEntity<?> getUser(@PathVariable UUID userId) {
        User user = userService.findUserById(userId);
        if (user == null) {
            return new ResponseEntity(new CustomErrorType("User with id " + userId + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(value = "/users/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable UUID userId, @RequestBody User user) {
        User currentUser = userService.findUserById(userId);

        if (currentUser == null) {
            return new ResponseEntity(new CustomErrorType("Unable to upate. User with id " + userId + " not found."), HttpStatus.NOT_FOUND);
        }

        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPassword(user.getPassword());
        currentUser.setActive(user.isActive());
        currentUser.setCreationDate(new Date());
        currentUser.setProfilePicture(user.getProfilePicture());

        userService.updateUser(currentUser);
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        User user = userService.findUserById(userId);
        if (user == null) {
            return new ResponseEntity(new CustomErrorType("Unable to delete. User with id " + userId + " not found."), HttpStatus.NOT_FOUND);
        }
        userService.deleteUser(userId);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }
}
