package com.example.security.controllers

import com.example.security.models.dtos.UserDto
import com.example.security.models.dtos.UserRegistrationDto
import com.example.security.services.UserService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.wrongwrong.mapk.core.KMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class AuthController(@Autowired val userService: UserService) {

    @GetMapping("/register")
    fun getRegister(): String {
        return "Registerpage"
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody user: UserRegistrationDto): ResponseEntity<UserDto> {
        return ResponseEntity(
            KMapper(::UserDto).map(userService.registerUser(user)),
            HttpStatus.CREATED
        )
    }

    @GetMapping("/login-page")
    fun loginPage(): ResponseEntity<String> {
        return ResponseEntity.ok(
            jacksonObjectMapper().writeValueAsString(
                mapOf("login" to "Please do a POST request to /api/login with credentials to login successfully.")
            )
        )
    }
}