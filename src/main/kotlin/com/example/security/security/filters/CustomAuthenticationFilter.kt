package com.example.security.security.filters

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.lang.RuntimeException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthenticationFilter(
    @Autowired private val authManager: AuthenticationManager
): UsernamePasswordAuthenticationFilter() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val username = request?.getParameter("username")
        val password = request?.getParameter("password")
        val authenticationToken = UsernamePasswordAuthenticationToken(username, password)
        val auth = authManager.authenticate(authenticationToken)

        if (!auth.isAuthenticated)
            throw RuntimeException("Could not authenticate")

        return auth
    }
}