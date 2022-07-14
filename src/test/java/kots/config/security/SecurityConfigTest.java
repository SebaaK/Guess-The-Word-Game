package kots.config.security;

import kots.model.Role;
import kots.model.User;
import kots.repository.RoleRepository;
import kots.repository.UserRepository;
import kots.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SecurityConfigTest {

    public static final String WORDS_BASE_ENDPOINT = "/api/words";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    void cleanUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void shouldLoginAndGetContent() throws Exception {
        // given
        userService.saveRole(new Role(1L, "USER"));
        userService.saveUser(new User(1L, "testUser", passwordEncoder.encode("password"), new HashSet<>()));
        userService.addRoleToUser("testUser", "USER");

        // when & then
        MvcResult login = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("username", "testUser")
                        .param("password", "password")
                )
                .andExpect(status().isOk())
                .andReturn();
        String token = login.getResponse().getHeader(AUTHORIZATION);

        mockMvc.perform(get(WORDS_BASE_ENDPOINT)
                .header(AUTHORIZATION, getBearerAuth(token))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void shouldReceiveStatusUnauthorizedWhenNoAuthorize() throws Exception {
        mockMvc.perform(get(WORDS_BASE_ENDPOINT))
                .andExpect(status().isUnauthorized());
    }

    private String getBearerAuth(String token) {
        return "Bearer " + token;
    }
}