package com.example.security.controllers

import com.example.security.models.dtos.UserDto
import com.example.security.models.dtos.UserRegistrationDto
import com.example.security.services.UserService
import com.wrongwrong.mapk.core.KMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/authentication")
class AuthController(@Autowired val userService: UserService) {

    @GetMapping("/register")
    fun getRegister(): String {
        return "Registerpage"
    }

    @GetMapping("/login")
    fun getLoginRedirect(req: HttpServletRequest, res: HttpServletResponse) {
        res.sendRedirect("/")
    }

    @PostMapping("/login")
    fun login(): Nothing? = null

    @PostMapping("/register")
    fun registerUser(@RequestBody user: UserRegistrationDto): ResponseEntity<UserDto> {
        return ResponseEntity(
            KMapper(::UserDto).map(userService.registerUser(user)),
            HttpStatus.CREATED
        )
    }
}