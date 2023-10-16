package com.iglo.chatbothelpdesk.controller;

import com.iglo.chatbothelpdesk.model.WebResponse;
import com.iglo.chatbothelpdesk.model.user.AuthRequest;
import com.iglo.chatbothelpdesk.model.user.UserRequest;
import com.iglo.chatbothelpdesk.model.user.UserResponse;
import com.iglo.chatbothelpdesk.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(
            path = "/auth",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> auth(@RequestBody AuthRequest request){
        UserResponse response = userService.auth(request);
        return WebResponse.<UserResponse>builder().data(response).build();
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserResponse> register(@RequestBody UserRequest request){
        UserResponse response = userService.register(request);
        return WebResponse.<UserResponse>builder().data(response).build();
    }



}
