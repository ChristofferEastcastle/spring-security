package com.example.security.controllers

import com.example.security.models.dtos.UserDto
import com.example.security.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/api/users/**")
class UserController(@Autowired val userService: UserService) {


    @GetMapping("/api/users")
    fun getUsers(): List<UserDto>{
        return userService.fetchAllUsers()
    }
}