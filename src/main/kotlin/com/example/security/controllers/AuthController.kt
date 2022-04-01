package com.example.security.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/auth")
class AuthController {

    @GetMapping("/login")
    fun loginPage(req: HttpServletRequest, res: HttpServletResponse): String {
        return "Should serve static HTML login form"
    }

}