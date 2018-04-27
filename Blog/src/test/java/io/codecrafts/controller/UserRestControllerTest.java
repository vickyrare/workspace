package io.codecrafts.controller;

import io.codecrafts.util.JsonUtil;
import io.codecrafts.model.Role;
import io.codecrafts.model.User;
import io.codecrafts.repository.RoleRepository;
import io.codecrafts.rest.UserRestController;
import io.codecrafts.service.UserRestService;
import io.codecrafts.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by waqqas on 4/25/2018.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = UserRestController.class, secure = false)
public class UserRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RoleRepository roleRepository;

    @Test
    public void givenUsers_whenGetUsers() throws Exception {
        Role role = new Role();
        role.setRole("USER");

        given(roleRepository.findByRole("USER")).willReturn(role);

        User user = new User();
        user.setFirstName("Waqqas");
        user.setLastName("Sharif");
        user.setEmail("vickyrare@yahoo.com");
        user.setPassword("12345678");
        user.setActive(true);
        user.setCreationDate(new Date());
        user.setProfilePicture("avatar.png");

        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));

        List<User> allUsers = Arrays.asList(user);

        given(userService.findAllInRange(0, 10)).willReturn(allUsers);

        mvc.perform(get("/api/users")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is(user.getFirstName())));
    }

    @Test
    public void whenPostUser_thenCreateUser() throws Exception {
        Role role = new Role();
        role.setRole("USER");

        given(roleRepository.findByRole("USER")).willReturn(role);

        User user = new User();
        user.setFirstName("Waqqas");
        user.setLastName("Sharif");
        user.setEmail("vickyrare@yahoo.com");
        user.setPassword("12345678");
        user.setActive(true);
        user.setCreationDate(new Date());
        user.setProfilePicture("avatar.png");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

        mvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void whenFindUser_thenReturnUser() throws Exception {
        UUID id = UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780556");

        Role role = new Role();
        role.setRole("USER");

        given(roleRepository.findByRole("USER")).willReturn(role);

        User user = new User();
        user.setId(id);
        user.setFirstName("Waqqas");
        user.setLastName("Sharif");
        user.setEmail("vickyrare@yahoo.com");
        user.setPassword("12345678");
        user.setActive(true);
        user.setCreationDate(new Date());
        user.setProfilePicture("avatar.png");

        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));

        given(userService.findUserById(id)).willReturn(user);

        mvc.perform(get("/api/users/" + id)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())));
    }

}
