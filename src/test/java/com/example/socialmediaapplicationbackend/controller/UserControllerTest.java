package com.example.socialmediaapplicationbackend.controller;

import com.example.socialmediaapplicationbackend.controller.request.UserJoinRequest;
import com.example.socialmediaapplicationbackend.controller.request.UserLoginRequest;
import com.example.socialmediaapplicationbackend.exception.ErrorCode;
import com.example.socialmediaapplicationbackend.exception.SimpleSnsApplicationException;
import com.example.socialmediaapplicationbackend.model.User;
import com.example.socialmediaapplicationbackend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithAnonymousUser
    public void join_success() throws Exception {
        String userName = "name";
        String password = "password";

        when(userService.join(userName, password)).thenReturn(mock(User.class));

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest("name", "password"))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void join_with_duplicated_username_should_fail() throws Exception {
        String userName = "name";
        String password = "password";
        when(userService.join(userName, password)).thenThrow(new SimpleSnsApplicationException(ErrorCode.DUPLICATED_USER_NAME));

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest("name", "password"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.DUPLICATED_USER_NAME.getStatus().value()));
    }

    @Test
    @WithAnonymousUser
    public void login_success() throws Exception {
        String userName = "name";
        String password = "password";

        when(userService.login(userName, password)).thenReturn("testToken");

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest("name", "password"))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void login_with_nonexistent_user_should_fail() throws Exception {
        String userName = "name";
        String password = "password";

        when(userService.login(userName, password)).thenThrow(new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest("name", "password"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.USER_NOT_FOUND.getStatus().value()));
    }

    @Test
    @WithAnonymousUser
    public void login_with_invalid_password_should_fail() throws Exception {
        String userName = "name";
        String password = "password";

        when(userService.login(userName, password)).thenThrow(new SimpleSnsApplicationException(ErrorCode.INVALID_PASSWORD));

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest("name", "password"))))
                .andDo(print())
                .andExpect(status().is(ErrorCode.INVALID_PASSWORD.getStatus().value()));
    }

}
