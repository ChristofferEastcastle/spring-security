package com.example.security.security

import com.example.security.security.filters.CustomAuthenticationFilter
import com.example.security.security.filters.CustomAuthorizationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@Configuration
class SecurityConfig(
    @Autowired private val userDetailsService: UserDetailsService,
    @Autowired private val passwordEncoder: BCryptPasswordEncoder,
): WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder)
    }

    override fun configure(http: HttpSecurity) {
        val authFilter = CustomAuthenticationFilter(authenticationManagerBean())
        http
            .addFilter(authFilter)
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .addFilterBefore(CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .formLogin()
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}