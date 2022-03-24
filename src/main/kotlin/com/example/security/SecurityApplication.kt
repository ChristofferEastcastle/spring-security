package com.example.security

import com.example.security.models.dtos.UserRegistrationDto
import com.example.security.models.entities.AuthorityEntity
import com.example.security.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
class SecurityApplication {

    @Bean
    fun init(@Autowired userService: UserService) = CommandLineRunner {
        val userAuthority = userService.createAuthority(AuthorityEntity(name = "USER"))
        val adminAuthority = userService.createAuthority(AuthorityEntity(name = "ADMIN"))
        val jimbob =
            UserRegistrationDto(
                username = "jim@bob.com",
                password = passwordEncoder().encode("pirate"))
        userService.registerUser(jimbob)
        val joebob =
            UserRegistrationDto(
                username = "joe@bob.com",
                password = passwordEncoder().encode("pirate"))
        userService.registerUser(joebob)
        userService.grantAuthorityToUser(jimbob.username, userAuthority.name)
        userService.grantAuthorityToUser(joebob.username, adminAuthority.name)
    }

    @Bean
    fun passwordEncoder() : BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}

fun main(args: Array<String>) {
    runApplication<SecurityApplication>(*args)
}