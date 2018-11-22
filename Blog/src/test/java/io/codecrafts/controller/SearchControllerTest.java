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
                .andExpect(view().name("admin/users"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("User Administration")))
                .andExpect(content().string(Matchers.containsString("vickyrare@gmail.com")));
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testSearchUsersByKeywordUsingGet() throws Exception{
        this.mockMvc.perform(get("/searchUser")
                .param("keyword", "admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("User Administration")))
                .andExpect(content().string(Matchers.containsString("vickyrare@gmail.com")));
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testSearchUsersByEmptyKeyword() throws Exception{
        this.mockMvc.perform(post("/searchUser")
                .param("keyword", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("User Administration")))
                .andExpect(content().string(Matchers.containsString("vickyrare@gmail.com")))
                .andExpect(content().string(Matchers.containsString("vickyrare@yahoo.com")))
                .andExpect(content().string(Matchers.containsString("anaya@yahoo.com")))
                .andExpect(content().string(Matchers.containsString("qirrat@yahoo.com")));
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testSearchUsersByEmptyKeywordUsingGet() throws Exception{
        this.mockMvc.perform(get("/searchUser")
                .param("keyword", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/users"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("User Administration")))
                .andExpect(content().string(Matchers.containsString("vickyrare@gmail.com")))
                .andExpect(content().string(Matchers.containsString("vickyrare@yahoo.com")))
                .andExpect(content().string(Matchers.containsString("anaya@yahoo.com")))
                .andExpect(content().string(Matchers.containsString("qirrat@yahoo.com")));
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testSearchPostsByKeyword() throws Exception{
        this.mockMvc.perform(post("/searchPost")
                .param("keyword", "PS3"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Posts")))
                .andExpect(content().string(Matchers.containsString("How to hack PS3")));
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testSearchPostsByKeywordUsingGet() throws Exception{
        this.mockMvc.perform(get("/searchPost")
                .param("keyword", "PS3"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Posts")))
                .andExpect(content().string(Matchers.containsString("How to hack PS3")));
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testSearchPostsByEmptyKeyword() throws Exception{
        this.mockMvc.perform(post("/searchPost")
                .param("keyword", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Posts")))
                .andExpect(content().string(Matchers.containsString("3DS")))
                .andExpect(content().string(Matchers.containsString("PS3")))
                .andExpect(content().string(Matchers.containsString("Wii U")));
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testSearchPostsByEmptyKeywordUsingGet() throws Exception{
        this.mockMvc.perform(get("/searchPost")
                .param("keyword", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Posts")))
                .andExpect(content().string(Matchers.containsString("3DS")))
                .andExpect(content().string(Matchers.containsString("PS3")))
                .andExpect(content().string(Matchers.containsString("Wii U")));
    }
}
