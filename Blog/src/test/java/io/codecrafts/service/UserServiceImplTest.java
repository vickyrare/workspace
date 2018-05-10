package io.codecrafts.service;

import io.codecrafts.model.Role;
import io.codecrafts.model.User;
import io.codecrafts.repository.RoleRepository;
import io.codecrafts.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by waqqas on 4/25/2018.
 */
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @TestConfiguration
    public static class UserServiceImplTestContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            return bCryptPasswordEncoder;
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() {
        User user = new User();

        user.setId(UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780556"));
        user.setFirstName("Waqqas");
        user.setLastName("Sharif");
        user.setEmail("vickyrare@yahoo.com");
        user.setPassword("12345678");
        user.setActive(true);
        user.setCreationDate(new Date());
        user.setProfilePicture("avatar.png");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(userRepository.findOne(user.getId())).thenReturn(user);

        User adminUser = new User();

        adminUser.setId(UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780556"));
        adminUser.setFirstName("Admin");
        adminUser.setLastName("User");
        adminUser.setEmail("vickyrare@gmail.com");
        adminUser.setPassword("12345678");
        adminUser.setActive(true);
        adminUser.setCreationDate(new Date());
        adminUser.setProfilePicture("avatar.png");

        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(adminUser);

        Page<User> pages = new PageImpl<User>(list);

        when(userRepository.findAll(new PageRequest(0, 2))).thenReturn(pages);

        Role userRole = roleRepository.findByRole("USER");

        when(roleRepository.findByRole("USER")).thenReturn(userRole);

        List<User> list2 = new ArrayList<>();
        list2.add(adminUser);

        when(userRepository.findAllByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContainingOrEmailIgnoreCaseContaining("admin", "admin", "admin", new PageRequest(0, 3))).thenReturn(list2);
        when(userRepository.findAllByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContainingOrEmailIgnoreCaseContaining("admin1", "admin1", "admin1", new PageRequest(0, 3))).thenReturn(null);
    }

    @Test
    public void whenValidEmail_thenUserShouldBeFound() {
        String email = "vickyrare@yahoo.com";
        User found = userService.findUserByEmail(email);

        assertThat(found.getEmail()).isEqualTo(email);

        verify(userRepository).findByEmail("vickyrare@yahoo.com");
    }

    @Test
    public void whenValidId_thenUserShouldBeFound() {
        UUID id = UUID.fromString("3ca1e527-d84b-49fc-a033-e27c59780556");
        User found = userService.findUserById(id);

        assertThat(found.getId()).isEqualTo(id);

        verify(userRepository).findOne(id);
    }

    @Test
    public void whenFindAll_thenAllUsersShouldBeFound() {
        List<User> users = userService.findAllInRange(0, 2);

        assertThat(users.get(0).getEmail()).isEqualTo("vickyrare@yahoo.com");
        assertThat(users.get(1).getEmail()).isEqualTo("vickyrare@gmail.com");

        verify(userRepository).findAll(new PageRequest(0, 2));
    }

    @Test
    public void whenFindAllByKeyword_thenUsersShouldBeFound() {
        List<User> users = userService.searchByKeyword("admin", new PageRequest(0, 3));

        assertThat(users.get(0).getFirstName()).isEqualTo("Admin");
        assertThat(users.get(0).getEmail()).isEqualTo("vickyrare@gmail.com");

        verify(userRepository).findAllByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContainingOrEmailIgnoreCaseContaining("admin", "admin", "admin", new PageRequest(0, 3));
    }

    @Test
    public void whenFindAllByKeyword_thenNoMatch() {
        List<User> users = userService.searchByKeyword("admin1", new PageRequest(0, 3));

        assertTrue(users == null);

        verify(userRepository).findAllByFirstNameIgnoreCaseContainingOrLastNameIgnoreCaseContainingOrEmailIgnoreCaseContaining("admin1", "admin1", "admin1", new PageRequest(0, 3));
    }
}
