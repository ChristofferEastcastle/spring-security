package com.example.security.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.jboss.logging.Logger
import org.springframework.boot.CommandLineRunner
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*

private var cookieSecret: String? = null

@Component
class CookieSecretInitializer: CommandLineRunner {
    override fun run(vararg args: String?) {
        cookieSecret = System.getenv()["COOKIE_SECRET"]
        if (cookieSecret.isNullOrBlank())
            throw RuntimeException("COOKIE_SECRET env variable must be set")
    }

}


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