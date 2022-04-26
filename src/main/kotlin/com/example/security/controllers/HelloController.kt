package com.example.security.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
class HelloController {

    @GetMapping("/")
    fun hello(): String {
        return "Hello!"
    }

    @GetMapping("/api")
    fun welcomeMessage(req: HttpServletRequest, res: HttpServletResponse) {
        jacksonObjectMapper().writeValue(
            res.writer,
            mapOf("welcome_message" to "Welcome to the API!")
        )
    }
}