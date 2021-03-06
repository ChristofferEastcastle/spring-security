package com.example.security.controllers

import com.example.security.models.dtos.UserDto
import com.example.security.models.entities.UserEntity
import com.example.security.services.UserService
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.wrongwrong.mapk.core.KMapper
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.reflect.KFunction

@RestController
@RequestMapping("/api/users")
class UserController(@Autowired val userService: UserService) {

    @GetMapping
    fun getUsers(): List<UserDto>{
        return userService.fetchAllUsers()
            .map { UserDto(it.id, it.username, it.enabled) }
            .toList()
    }

    @GetMapping("{id}")
    fun getUser(@PathVariable id: Long): ResponseEntity<UserDto> {
        val mapper: KMapper<UserDto> = KMapper(::UserDto)
        val user = userService.getUser(id)
        user?.let { return ResponseEntity.ok().body(mapper.map(user)) }

        return ResponseEntity.notFound().build()
    }
}