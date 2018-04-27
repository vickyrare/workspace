package io.codecrafts.respository;

import io.codecrafts.model.Role;
import io.codecrafts.model.User;
import io.codecrafts.repository.RoleRepository;
import io.codecrafts.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by waqqas on 4/25/2018.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRespositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void whenFindByEmail_thenReturnUser() {
        User user = new User();
        user.setFirstName("Waqqas");
        user.setLastName("Sharif");
        user.setEmail("vickyrare@yahoo.com");
        user.setPassword("12345678");
        user.setActive(true);
        user.setCreationDate(new Date());
        user.setProfilePicture("avatar.png");

        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

        userRepository.save(user);

        User found = userRepository.findByEmail(user.getEmail());

        assertThat(found.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(found.getLastName()).isEqualTo(user.getLastName());
        assertThat(found.getEmail()).isEqualTo(user.getEmail());
        assertThat(found.getPassword()).isEqualTo(user.getPassword());
        assertThat(found.isActive()).isEqualTo(user.isActive());
        assertThat(found.getProfilePicture()).isEqualTo(user.getProfilePicture());
        assertThat(found.getCreationDate()).isEqualTo(user.getCreationDate());
        assertThat(found.getRoles()).isEqualTo(user.getRoles());
        assertThat(found.isAdmin()).isEqualTo(user.isAdmin());
    }

    @Test
    public void whenFindById_thenReturnUser() {
        User user = new User();
        user.setFirstName("Waqqas");
        user.setLastName("Sharif");
        user.setEmail("vickyrare@yahoo.com");
        user.setPassword("12345678");
        user.setActive(false);
        user.setCreationDate(new Date());
        user.setProfilePicture("avatar.png");

        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

        userRepository.save(user);

        User found = userRepository.findOne(user.getId());

        assertThat(found.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(found.getLastName()).isEqualTo(user.getLastName());
        assertThat(found.getEmail()).isEqualTo(user.getEmail());
        assertThat(found.getPassword()).isEqualTo(user.getPassword());
        assertThat(found.isActive()).isEqualTo(user.isActive());
        assertThat(found.getProfilePicture()).isEqualTo(user.getProfilePicture());
        assertThat(found.getCreationDate()).isEqualTo(user.getCreationDate());
        assertThat(found.getRoles()).isEqualTo(user.getRoles());
        assertThat(found.isAdmin()).isEqualTo(user.isAdmin());
    }

    @Test
    public void whenAdminFindByEmail_thenReturnUser() {
        User adminUser = new User();
        adminUser.setFirstName("Waqqas");
        adminUser.setLastName("Sharif");
        adminUser.setEmail("vickyrare@gmail.com");
        adminUser.setPassword("12345678");
        adminUser.setActive(true);
        adminUser.setCreationDate(new Date());
        adminUser.setProfilePicture("avatar.png");

        Role adminRole = roleRepository.findByRole("ADMIN");
        adminUser.setRoles(new HashSet<Role>(Arrays.asList(adminRole)));

        userRepository.save(adminUser);

        User found = userRepository.findOne(adminUser.getId());

        assertThat(found.getFirstName()).isEqualTo(adminUser.getFirstName());
        assertThat(found.getLastName()).isEqualTo(adminUser.getLastName());
        assertThat(found.getEmail()).isEqualTo(adminUser.getEmail());
        assertThat(found.getPassword()).isEqualTo(adminUser.getPassword());
        assertThat(found.isActive()).isEqualTo(adminUser.isActive());
        assertThat(found.getProfilePicture()).isEqualTo(adminUser.getProfilePicture());
        assertThat(found.getCreationDate()).isEqualTo(adminUser.getCreationDate());
        assertThat(found.getRoles()).isEqualTo(adminUser.getRoles());
        assertThat(found.isAdmin()).isEqualTo(adminUser.isAdmin());
    }
}
