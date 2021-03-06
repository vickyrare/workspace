package io.codecrafts.controller;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
public class SettingsControllerTest {

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
    public void testUserTriesToViewItsOwnSettings() throws Exception {
        this.mockMvc.perform(get("/settings"))
                .andExpect(status().isOk())
                .andExpect(view().name("settings"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Settings")))
                .andExpect(content().string(Matchers.containsString("vickyrare@yahoo.com")));
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testUserTriesToEditItsOwnSettings() throws Exception {
        MockMultipartFile mockMultipartFile = getMockMultipartFile();
        this.mockMvc.perform(MockMvcRequestBuilders.fileUpload("/settings")
                                     .file(mockMultipartFile)
                                     .param("firstName", "Omair")
                                     .param("lastName", "Sharif")
                                     .param("password", "12345678")
                                     .param("email", "vickyrare@yahoo.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Posts")))
                .andExpect(content().string(Matchers.containsString("Omair")))
                .andExpect(content().string(Matchers.containsString("avatar.png")));
    }

    @Test
    @WithUserDetails("vickyrare@yahoo.com")
    public void testUserTriesToEditOtherUserSettings() throws Exception {
        MockMultipartFile mockMultipartFile = getMockMultipartFile();
        this.mockMvc.perform(MockMvcRequestBuilders.fileUpload("/settings")
                                     .file(mockMultipartFile)
                                     .param("firstName", "Omair")
                                     .param("lastName", "Sharif")
                                     .param("password", "12345678")
                                     .param("email", "vickyrare@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Posts")))
                .andExpect(content().string(Matchers.containsString("Waqqas")));
    }

    private MockMultipartFile getMockMultipartFile() throws IOException {
        Resource resource = new ClassPathResource("/static/images/avatar.png");
        File file = resource.getFile();
        FileInputStream fileInputStream = new FileInputStream(file);
        return new MockMultipartFile("file", file.getName(), "multipart/form-data", fileInputStream);
    }
}
