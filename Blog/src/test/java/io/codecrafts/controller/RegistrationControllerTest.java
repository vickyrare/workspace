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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
public class RegistrationControllerTest {

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
    public void testRegistrationForm() throws Exception {
        this.mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("title"))
                .andExpect(MockMvcResultMatchers.model().attribute("title", is("Registration")))
                .andDo(print());
    }

    @Test
    public void testRegistrationWithAllInformation() throws Exception {
        MockMultipartFile mockMultipartFile = getMockMultipartFile();
        this.mockMvc.perform(MockMvcRequestBuilders.fileUpload("/registration")
                                .file(mockMultipartFile)
                                .param("firstName","Test")
                                .param("lastName","User")
                                .param("email", "test@yahoo.com")
                                .param("password", "12345"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(content().string(Matchers.containsString("User has been registered successfully")));
    }

    @Test
    public void testRegistrationEmptyFirstNameLastNameEmptyPasswordEmptyEmail() throws Exception {
        MockMultipartFile mockMultipartFile = getMockMultipartFile();
        this.mockMvc.perform(MockMvcRequestBuilders.fileUpload("/registration")
                                     .file(mockMultipartFile)
                                     .param("firstName","")
                                     .param("lastName","")
                                     .param("email", "")
                                     .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(content().string(Matchers.containsString("Please provide your first name")))
                .andExpect(content().string(Matchers.containsString("Please provide your last name")))
                .andExpect(content().string(Matchers.containsString("Please provide an email")))
                .andExpect(content().string(Matchers.containsString("Your password must have at least 5 characters")));
    }

    @Test
    public void testRegistrationEmailAlreadyInUse() throws Exception {
        MockMultipartFile mockMultipartFile = getMockMultipartFile();
        this.mockMvc.perform(MockMvcRequestBuilders.fileUpload("/registration")
                                     .file(mockMultipartFile)
                                     .param("firstName","Test")
                                     .param("lastName","User")
                                     .param("email", "vickyrare@yahoo.com")
                                     .param("password", "12345"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration"))
                .andExpect(content().string(Matchers.containsString("There is already a user registered with the email provided")));
    }

    private MockMultipartFile getMockMultipartFile() throws IOException {
        Resource resource = new ClassPathResource("/static/images/avatar.png");
        File file = resource.getFile();
        FileInputStream fileInputStream = new FileInputStream(file);
        return new MockMultipartFile("file", file.getName(), "multipart/form-data", fileInputStream);
    }
}
