package com.example.security.controllers

import com.example.security.models.dtos.UserDto
import com.example.security.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(@Autowired val userService: UserService) {


    @GetMapping
    fun getUsers(): List<UserDto>{
        return userService.fetchAllUsers()
            .map { UserDto(it.id, it.username, it.enabled) }
            .toList()
    }
}