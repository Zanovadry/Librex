package org.example.librex.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.librex.database.users.AppUserRepository;
import org.example.librex.database.users.dto.RegistrationRequest;
import org.example.librex.database.users.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class UserIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AppUserRepository userRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        userRepository.deleteAll();
    }

    @Test
    void testUserRegistration() throws Exception {
        RegistrationRequest req = new RegistrationRequestTestFactory().validUser();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.username").value("tester"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetAllUsers() throws Exception {

        RegistrationRequest req = new RegistrationRequestTestFactory().validUser();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateUser() throws Exception {

        RegistrationRequest req = new RegistrationRequestTestFactory().validUser();

        String result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andReturn().getResponse().getContentAsString();

        UserResponse created = objectMapper.readValue(result, UserResponse.class);

        assert created.getId() != null;

        RegistrationRequest update = new RegistrationRequestTestFactory().updatedUser();

        mockMvc.perform(put("/api/users/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("UpdatedName"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteUser() throws Exception {

        RegistrationRequest req = new RegistrationRequestTestFactory().validUser();

        String result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andReturn().getResponse().getContentAsString();

        UserResponse created = objectMapper.readValue(result, UserResponse.class);

        assert created.getId() != null;

        mockMvc.perform(delete("/api/users/" + created.getId()))
                .andExpect(status().isNoContent());
    }
}
