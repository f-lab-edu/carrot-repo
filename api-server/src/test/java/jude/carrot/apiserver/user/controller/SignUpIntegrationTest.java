package jude.carrot.apiserver.user.controller;

import jude.carrot.apiserver.user.dto.request.UserRequest.SignUpRequest;
import jude.carrot.apiserver.user.repository.UserRepository;
import jude.carrot.apiserver.user.repository.jpa.UserJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Testcontainers
class SignUpIntegrationTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    @Autowired
    WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserJpaRepository userJpaRepository;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @AfterEach
    void tearDown() {
        userJpaRepository.deleteAll();
    }

    private SignUpRequest request(String email, String password) {
        return SignUpRequest.builder()
                .email(email)
                .password(password)
                .build();
    }

    @Test
    @DisplayName("정상 회원가입 - 200 반환")
    void signUp_success() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request("test@example.com", "Password1!"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SC"));
    }

    @Test
    @DisplayName("이메일 중복 - 409 반환")
    void signUp_duplicateEmail_conflict() throws Exception {
        userRepository.save("dup@example.com", "Password1!");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request("dup@example.com", "Password1!"))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("UE"));
    }

    @Test
    @DisplayName("이메일 형식 오류 - 400 반환")
    void signUp_invalidEmail_badRequest() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request("not-an-email", "Password1!"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("IR"));
    }

    @Test
    @DisplayName("비밀번호 조건 미충족 - 400 반환")
    void signUp_weakPassword_badRequest() throws Exception {
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request("test@example.com", "weakpass"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("IR"));
    }
}
