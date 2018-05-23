package io.codecrafts.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
public class LoginControllerTest {

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
    public void testIndex() throws Exception{
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Login")));
    }

    @Test
    public void tryToAccessSystemWithoutLogin() throws Exception{
        this.mockMvc.perform(get("/posts"))
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void testLogout() throws Exception{
        //signin
        this.mockMvc.perform(post("/signin")
                .param("email", "qirrat@yahoo.com")
                .param("password", "12345678"))
                .andExpect(redirectedUrl("posts"));

        //logout
        this.mockMvc.perform(post("/logout"))
                .andExpect(redirectedUrl("/"));

        //try to access system after logout
        this.mockMvc.perform(get("/posts"))
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void testUserLoginWithCorrectCredentials() throws Exception{
        this.mockMvc.perform(post("/signin")
                                     .param("email", "qirrat@yahoo.com")
                                     .param("password", "12345678"))
                .andExpect(redirectedUrl("posts"));
    }

    @Test
    public void testUserLoginWithIncorrectCredentials() throws Exception{
        this.mockMvc.perform(post("/signin")
                                     .param("email", "qirrat@gmail.com")
                                     .param("password", "123456"))
                .andExpect(redirectedUrl("/login?error=true"));
    }

    @Test
    public void testAdminLoginWithCorrectCredentials() throws Exception{
        this.mockMvc.perform(post("/signin")
                                     .param("email", "vickyrare@gmail.com")
                                     .param("password", "12345678"))
                .andExpect(redirectedUrl("/admin/users"));
    }

    @Test
    public void testAdminLoginWithIncorrectCredentials() throws Exception{
        this.mockMvc.perform(post("/signin")
                                     .param("email", "vickyrare@gmail.com")
                                     .param("password", "123456"))
                .andExpect(redirectedUrl("/login?error=true"));
    }
}
