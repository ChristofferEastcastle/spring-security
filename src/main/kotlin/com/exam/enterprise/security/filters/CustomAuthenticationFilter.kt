package com.exam.enterprise.security.filters

import com.exam.enterprise.utils.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthenticationFilter(
    @Autowired private val authManager: AuthenticationManager,
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val username = request?.getParameter("username")
        val password = request?.getParameter("password")
        val authenticationToken = UsernamePasswordAuthenticationToken(username, password)
        return authManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val user = authResult.principal as User
        val accessToken = JwtUtil.createToken(user, issuer = request.servletPath, minutes = 10)
        val cookie = Cookie("access_token", accessToken)
        cookie.path = "/api"
        response.addCookie(cookie)
        chain.doFilter(request, response)
    }
}