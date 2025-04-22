package com.example.socialmediaapplicationbackend.controller;

import com.example.socialmediaapplicationbackend.controller.request.UserJoinRequest;
import com.example.socialmediaapplicationbackend.controller.response.Response;
import com.example.socialmediaapplicationbackend.controller.response.UserJoinResponse;
import com.example.socialmediaapplicationbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
