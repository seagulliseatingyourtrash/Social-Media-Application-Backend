package com.example.socialmediaapplicationbackend.controller;

import com.example.socialmediaapplicationbackend.controller.request.UserJoinRequest;
import com.example.socialmediaapplicationbackend.controller.request.UserLoginRequest;
import com.example.socialmediaapplicationbackend.controller.response.Response;
import com.example.socialmediaapplicationbackend.controller.response.UserJoinResponse;
import com.example.socialmediaapplicationbackend.controller.response.UserLoginResponse;
import com.example.socialmediaapplicationbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        return Response.success(UserJoinResponse.fromUser(userService.join(request.getName(), request.getPassword())));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

    @GetMapping("/me")
    public Response<UserJoinResponse> me(Authentication authentication) {
        return Response.success(UserJoinResponse.fromUser(userService.loadUserByUsername(authentication.getName())));
    }

}
