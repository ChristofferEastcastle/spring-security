package com.example.security.controllers

import com.example.security.models.dtos.UserDto
import com.example.security.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController("/api/user")
class UserController(@Autowired val userService: UserService) {

    fun getUsers(): List<UserDto>{
        return userService.fetchAllUsers()
    }
}