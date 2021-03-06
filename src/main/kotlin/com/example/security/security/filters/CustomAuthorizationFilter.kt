package com.example.security.security.filters

import com.example.security.services.UserService
import com.example.security.utils.JwtUtil
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthorizationFilter(
    @Autowired private val userService: UserService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.cookies?.firstOrNull { it.name == "access_token" }

        if (token == null) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val decodedToken = JwtUtil.decodeToken(token = token.value)
            val username = decodedToken.subject
            // This depends on unique username for the whole system
            userService.loadUserByUsername(username)

            val authority = decodedToken.getClaim("authorities").asList(String::class.java)
                .map { SimpleGrantedAuthority(it) }
            val authToken = UsernamePasswordAuthenticationToken(username, null, authority)
            SecurityContextHolder.getContext().authentication = authToken
            filterChain.doFilter(request, response)

        } catch (e: Exception) {
            val error = when (e) {
                is UsernameNotFoundException -> mapOf("error_message" to e.message)
                else -> mapOf("error_message" to "access token invalid!")
            }
            logger.error("Authorization ERROR: ${e.message}")
            response.contentType = APPLICATION_JSON_VALUE
            response.status = UNAUTHORIZED.value()
            val cookie = Cookie("access_token", null)
            cookie.maxAge = 0
            response.addCookie(cookie)
            jacksonObjectMapper().writeValue(response.outputStream, error)
        }
    }
}