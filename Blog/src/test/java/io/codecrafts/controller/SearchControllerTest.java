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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
public class SearchControllerTest {

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
    @WithUserDetails("vickyrare@yahoo.com")
    public void testSearchUsersByKeyword() throws Exception{
        this.mockMvc.perform(post("/searchUser")
                .param("keyword", "admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("/admin/users"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("User Administration")))
                .andExpect(content().string(Matchers.containsString("vickyrare@gmail.com")));
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testSearchPostsByKeyword() throws Exception{
        this.mockMvc.perform(post("/searchPost")
                .param("keyword", "hack"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Posts")))
                .andExpect(content().string(Matchers.containsString("How to hack Wii U Modified")))
                .andExpect(content().string(Matchers.containsString("How to hack 3DS")))
                .andExpect(content().string(Matchers.containsString("How to hack PS3")));
    }
}
