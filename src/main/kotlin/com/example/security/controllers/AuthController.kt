package com.example.security.controllers

import com.example.security.models.dtos.UserDto
import com.example.security.models.dtos.UserRegistrationDto
import com.example.security.services.UserService
import com.wrongwrong.mapk.core.KMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/authentication")
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
}