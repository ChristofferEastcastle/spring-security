package com.example.security.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.boot.CommandLineRunner
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*

var cookieSecret: String? = null

@Component
class CookieSecretInitializer: CommandLineRunner {
    override fun run(vararg args: String?) {
        getCookieSecret()
    }
}

@JvmName("getCookieSecret1")
private fun getCookieSecret() {
    cookieSecret = System.getenv()["COOKIE_SECRET"]
    if (cookieSecret.isNullOrBlank())
        throw RuntimeException("COOKIE_SECRET env variable must be set")
}

object JwtUtil {
    fun createToken(user: User, issuer: String, minutes: Int): String {
        getCookieSecret()
        val algorithm = Algorithm.HMAC256(cookieSecret)
        return JWT.create()
            .withSubject(user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + minutes * 60 * 1000))
            .withIssuer(issuer)
            .withClaim("authorities", user.authorities.map { it.authority })
            .sign(algorithm)
    }

    fun decodeToken(token: String): DecodedJWT {
        getCookieSecret()
        val algorithm = Algorithm.HMAC256(cookieSecret)
        val jwtVerifier = JWT.require(algorithm).build()
        return jwtVerifier.verify(token)
    }
}