package com.example.security.controllers

import com.example.security.security.SecurityConfig.Companion.LOGIN_URL
import com.example.security.security.SecurityConfig.Companion.REGISTER_URL
import com.example.security.exceptions.UsernameExistsException
import com.example.security.models.dtos.UserDto
import com.example.security.models.dtos.UserRegistrationDto
import com.example.security.services.UserService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.wrongwrong.mapk.core.KMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/authentication")
class AuthController(@Autowired val userService: UserService) {

    @GetMapping("/register")
    fun getRegister(): ResponseEntity<String> {
        return ResponseEntity.ok(
            jacksonObjectMapper().writeValueAsString(
                mapOf("register" to "Please do a POST request to $REGISTER_URL with credentials to register successfully.")
            )
        )
    }

    @PostMapping("/register")
    fun registerUser(
        @RequestBody user: UserRegistrationDto,
        res: HttpServletResponse
    ): ResponseEntity<Any> {
        return try {
            val registered = userService.registerUser(user)
            ResponseEntity(KMapper(::UserDto).map(registered), CREATED)
        } catch (e: UsernameExistsException) {
            ResponseEntity.badRequest()
                .body(mapOf("error_message" to e.message))
        }
    }

    @GetMapping("/login-page")
    fun loginPage(): ResponseEntity<String> {
        return ResponseEntity.ok(
            jacksonObjectMapper().writeValueAsString(
                mapOf("login" to "Please do a POST request to $LOGIN_URL with credentials to login successfully.")
            )
        )
    }
}