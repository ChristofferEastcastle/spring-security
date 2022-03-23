package com.example.security.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController("/api/auth")
class AuthenticationController {

    @GetMapping("/api/auth/logout")
    fun logout(request: HttpServletRequest, response: HttpServletResponse) {
        val cookie = Cookie("access_token", "null")
        cookie.maxAge = 0
        response.addCookie(cookie)
        response.sendRedirect("/api/auth/login")
    }
}