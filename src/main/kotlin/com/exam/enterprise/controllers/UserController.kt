package com.exam.enterprise.controllers

import com.exam.enterprise.models.dtos.UserDto
import com.exam.enterprise.services.UserService
import com.wrongwrong.mapk.core.KMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(@Autowired val userService: UserService) {

    @GetMapping
    fun getUsers(): List<UserDto> {
        return userService.fetchAllUsers()
            .map { KMapper(::UserDto).map(it) }
            .toList()
    }

    @GetMapping("{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<UserDto> {
        val user = userService.getUser(id)
        user?.let {
            return ResponseEntity(KMapper(::UserDto).map(user), OK)
        }

        return ResponseEntity(NOT_FOUND)
    }
}