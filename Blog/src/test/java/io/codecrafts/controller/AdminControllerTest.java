package io.codecrafts.controller;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
public class AdminControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails("vickyrare@gmail.com")
    public void testUserAdministrationIndexPage() throws Exception{
        this.mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("User Administration")))
                .andExpect(content().string(Matchers.containsString("<td>79a5bd3f-1ec2-46cf-94b6-8ac23df3f3c9</td>\n" +
                                                                    "\t\t\t\t\t\t\t<td>Admin</td>\n" +
                                                                    "\t\t\t\t\t\t\t<td>User</td>\n" +
                                                                    "\t\t\t\t\t\t\t<td>vickyrare@gmail.com</td>")));
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testUserTriesToAccessUserAdministrationIndexPage() throws Exception{
        this.mockMvc.perform(get("/admin/users"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithUserDetails("vickyrare@gmail.com")
    public void testAdminTriesToDisableAUser() throws Exception{
        this.mockMvc.perform(get("/admin/users/" + "f9d98297-9db9-41a3-86e6-25ab0480fcd8" + "/disable"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("User Administration")))
                .andExpect(content().string(Matchers.containsString("/admin/users/f9d98297-9db9-41a3-86e6-25ab0480fcd8/enable")));
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testUserTriesToDisableAUser() throws Exception{
        this.mockMvc.perform(get("/admin/users/" + "f9d98297-9db9-41a3-86e6-25ab0480fcd8" + "/disable"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithUserDetails("vickyrare@gmail.com")
    public void testAdminTriesToEnableAUser() throws Exception{
        this.mockMvc.perform(get("/admin/users/" + "a9d98297-9db9-41a3-86e6-25ab0480fcd8" + "/enable"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("User Administration")))
                .andExpect(content().string(Matchers.containsString("/admin/users/a9d98297-9db9-41a3-86e6-25ab0480fcd8/disable")));
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testUserTriesToEnableAUser() throws Exception{
        this.mockMvc.perform(get("/admin/users/" + "a9d98297-9db9-41a3-86e6-25ab0480fcd8" + "/enable"))
                .andExpect(status().is4xxClientError());
    }
}
