package com.example.security.configs

import com.example.security.repos.UserRepo
import com.example.security.services.UserService
import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@TestConfiguration
@Order(1)
class ControllerTestConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http.csrf().disable().authorizeRequests().anyRequest().permitAll()
    }

    @Bean
    fun userService() = mockk<UserService>()

    @Bean
    fun userRepo() = mockk<UserRepo>()
}