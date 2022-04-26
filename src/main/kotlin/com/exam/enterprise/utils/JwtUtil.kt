package com.exam.enterprise.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.security.core.userdetails.User
import java.util.*


private var cookieSecret = "dlkjaslfjfadjslfjdsiuniunbrimf"


object JwtUtil {
    private val algorithm = Algorithm.HMAC256(cookieSecret)
    fun createToken(user: User, issuer: String, minutes: Int): String {
        return JWT.create()
            .withSubject(user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + minutes * 60 * 1000))
            .withIssuer(issuer)
            .withClaim("authorities", user.authorities.map { it.authority })
            .sign(algorithm)
    }

    fun decodeToken(token: String): DecodedJWT {
        val jwtVerifier = JWT.require(algorithm).build()
        return jwtVerifier.verify(token)
    }
}