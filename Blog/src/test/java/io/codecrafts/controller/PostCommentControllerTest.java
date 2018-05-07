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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
public class PostCommentControllerTest {

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
    public void testListPostComments() throws Exception{
        this.mockMvc.perform(get("/posts/e63d1cf0-b70e-43f1-bf4c-5f562d1c5a59/comments"))
                .andExpect(status().isOk())
                .andExpect(view().name("comments"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Comments")))
                .andExpect(content().string(Matchers.containsString("I am wondering whether anyone can help me hack my Wii U. My Wii U is currently running 1.5 firmware version.")))
                .andDo(print());
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testNewPostComment() throws Exception{
        this.mockMvc.perform(post("/posts/e63d1cf0-b70e-43f1-bf4c-5f562d1c5a59/comments")
                                     .param("content", "Visit Modified http://www.wiiu.guide.org"))
                .andExpect(status().isOk())
                .andExpect(view().name("comments"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Comments")))
                .andExpect(content().string(Matchers.containsString("Visit Modified http://www.wiiu.guide.org")))
                .andDo(print());
    }

    @Test
    @WithUserDetails("vickyrare@gmail.com")
    public void testEditPostCommentForm() throws Exception{
        this.mockMvc.perform(get("/posts/d63d1cf0-b70e-43f1-bf4c-5f562d1c5a59/comments/dfdaf9b9-75f9-4ebe-ab6d-307f315cef65/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("commenteditform"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Edit Comment")))
                .andDo(print());
    }

    @Test
    @WithUserDetails("vickyrare@gmail.com")
    public void testUserTriesToEditItsOwnPostComment() throws Exception{
        this.mockMvc.perform(post("/posts/d63d1cf0-b70e-43f1-bf4c-5f562d1c5a59/comments/dfdaf9b9-75f9-4ebe-ab6d-307f315cef65")
                                     .param("content", "Try modified IGN"))
                .andExpect(status().isOk())
                .andExpect(view().name("comments"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Comments")))
                .andExpect(content().string(Matchers.containsString("Try modified IGN")))
                .andDo(print());
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testUserTriesToEditOtherUserPostComment() throws Exception{
        this.mockMvc.perform(post("/posts/d63d1cf0-b70e-43f1-bf4c-5f562d1c5a59/comments/dfdaf9b9-75f9-4ebe-ab6d-307f315cef65")
                                     .param("content", "Try modified IGN"))
                .andExpect(status().isOk())
                .andExpect(view().name("comments"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Comments")))
                .andExpect(content().string(Matchers.containsString("Try IGN")))
                .andDo(print());
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testUserTriesToDeleteItsOwnPostComment() throws Exception{
        this.mockMvc.perform(get("/posts/e63d1cf0-b70e-43f1-bf4c-5f562d1c5a59/comments/cfdaf9b9-75f9-4ebe-ab6d-307f315cef65/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("comments"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Comments")))
                .andDo(print());
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testUserTriesToDeleteOtherUserPostComment() throws Exception{
        this.mockMvc.perform(get("/posts/f63d1cf0-b70e-43f1-bf4c-5f562d1c5a59/comments/efdaf9b9-75f9-4ebe-ab6d-307f315cef65/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("comments"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Comments")))
                .andExpect(content().string(Matchers.containsString("Try Gamespot")))
                .andDo(print());
    }
}
