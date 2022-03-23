package com.example.security.security.filters

import com.example.security.security.AdditionalFormLoginConfigurer.Companion.LOGIN_PAGE_URL
import com.example.security.security.jwt.JwtUtil
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CustomAuthorizationFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.cookies?.firstOrNull { it.name == "access_token" }

        when {
            token == null -> {
                filterChain.doFilter(request, response)
            }
            request.servletPath.contains(LOGIN_PAGE_URL) -> {
                filterChain.doFilter(request, response)
            }
            request.servletPath.contains("/register") -> {
                filterChain.doFilter(request, response)
            }
            else -> {
                try {
                    val decodedToken = JwtUtil.decodeToken(token.value)
                    val username = decodedToken.subject
                    val authority = decodedToken.getClaim("authorities").asList(String::class.java)
                        .map { SimpleGrantedAuthority(it) }
                    val authToken = UsernamePasswordAuthenticationToken(username, null, authority)
                    SecurityContextHolder.getContext().authentication = authToken
                    filterChain.doFilter(request, response)

                } catch (e: Exception) {
                    logger.error("Authorization ERROR: ${e.message}")
                    response.contentType = APPLICATION_JSON_VALUE
                    response.status = FORBIDDEN.value()
                    val error = mapOf("error_message" to e.message)
                    response.addCookie(Cookie("access_token", null))
                    filterChain.doFilter(request, response)
                    //response.sendRedirect(LOGOUT_PAGE)
                    jacksonObjectMapper().writeValue(response.outputStream, error)
                }
            }
        }
    }
}