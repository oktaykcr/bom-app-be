package com.oktaykcr.bomappbe.controller;

import com.oktaykcr.bomappbe.common.ApiResponse;
import com.oktaykcr.bomappbe.model.user.User;
import com.oktaykcr.bomappbe.service.CustomUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final CustomUserDetailsServiceImpl userDetailsService;

    @Autowired
    public UserController(CustomUserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(value = "/register")
    public ApiResponse<Boolean> register(@RequestBody User user) {
        return ApiResponse.response(userDetailsService.save(user));
    }

    @PostMapping(value = "/validate")
    public ApiResponse<Boolean> validate(@RequestParam("token") String token) {
        return ApiResponse.response(userDetailsService.isTokenValid(token));
    }

}
