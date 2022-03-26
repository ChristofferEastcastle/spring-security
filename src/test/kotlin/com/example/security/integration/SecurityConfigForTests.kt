package com.example.security.integration

import com.example.security.security.SecurityConfig
import com.example.security.security.SecurityConfig.Authorities.ADMIN
import com.example.security.security.SecurityConfig.Authorities.USER
import com.example.security.security.filters.CustomAuthenticationFilter
import com.example.security.security.filters.CustomAuthorizationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.test.context.ActiveProfiles
import java.util.*


@Configuration
@ActiveProfiles("test")
class SecurityConfigForTests(
) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        val passwordEncoder = BCryptPasswordEncoder()
        auth
            .inMemoryAuthentication()
            .passwordEncoder(passwordEncoder)
            .withUser("security")
            .password(passwordEncoder.encode("password"))
            .authorities(USER.name, ADMIN.name)
    }

    override fun configure(http: HttpSecurity) {
        val authFilter = CustomAuthenticationFilter(authenticationManagerBean())
        http
            .csrf().disable()
            .addFilter(authFilter)
            .sessionManagement()
            // No need for session because we are using our own JWT to control this.
            .sessionCreationPolicy(SessionCreationPolicy.NEVER)
            .and()
            .authorizeRequests()
            .antMatchers("/api/users/**").hasAuthority(ADMIN.name)
            .anyRequest()
            .authenticated()
            .and()
            .addFilterBefore(CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .formLogin()
            .and()
            .logout()
            .deleteCookies("access_token")
    }

    @Bean(autowireCandidate = false)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }
}