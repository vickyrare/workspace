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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
public class PostControllerTest {

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
    public void testListPosts() throws Exception{
        this.mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Posts")))
                .andExpect(content().string(Matchers.containsString("How to hack Wii U")))
                .andDo(print());
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testNewPostForm() throws Exception{
        this.mockMvc.perform(get("/posts/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("postform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("New Post")))
                .andDo(print());
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testNewPost() throws Exception{
        this.mockMvc.perform(post("/posts")
                                     .param("title", "New Post Title")
                                     .param("description", "New Post Description"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Posts")))
                .andExpect(content().string(Matchers.containsString("New Post Title")))
                .andDo(print());
    }

    @Test
    @WithUserDetails("vickyrare@gmail.com")
    public void testEditPostForm() throws Exception{
        this.mockMvc.perform(get("/posts/d63d1cf0-b70e-43f1-bf4c-5f562d1c5a59/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("posteditform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Edit Post")))
                .andDo(print());
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testUserTriesToEditItsOwnPost() throws Exception{
        this.mockMvc.perform(post("/posts/e63d1cf0-b70e-43f1-bf4c-5f562d1c5a59")
                                     .param("title", "How to hack Wii U Modified")
                                     .param("description", "I am wondering whether anyone can help me hack my Wii U. My Wii U is currently running 1.5 firmware version."))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Posts")))
                .andExpect(content().string(Matchers.containsString("How to hack Wii U Modified")))
                .andDo(print());
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testUserTriesToEditOtherUserPost() throws Exception{
        this.mockMvc.perform(post("/posts/f63d1cf0-b70e-43f1-bf4c-5f562d1c5a59")
                                     .param("title", "How Modified to hack PS3")
                                     .param("description", "I am wondering whether anyone can help me hack my PS3. My PS3 is currently running 1.5 firmware version."))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Posts")))
                .andExpect(content().string(Matchers.containsString("How to hack PS3")))
                .andDo(print());
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testUserTriesToDeleteItsOwnPost() throws Exception{
        this.mockMvc.perform(get("/posts/e73d1cf0-b70e-43f1-bf4c-5f562d1c5a59/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Posts")))
                .andDo(print());
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testUserTriesToDeleteOtherUserPost() throws Exception{
        this.mockMvc.perform(get("/posts/f63d1cf0-b70e-43f1-bf4c-5f562d1c5a59/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Posts")))
                .andExpect(content().string(Matchers.containsString("How to hack PS3")))
                .andDo(print());
    }
}
