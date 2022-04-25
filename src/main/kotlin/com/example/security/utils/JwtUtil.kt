package com.example.security.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.example.security.exceptions.EnvironmentVariableNotSetException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.core.env.Environment
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*


private var cookieSecret: String? = null

@Component
class CookieSecretInitializer: CommandLineRunner {
    @Autowired
    lateinit var env: Environment

    private val cookieSecretStr = "COOKIE_SECRET"

    override fun run(vararg args: String?) {
        cookieSecret = if (env.activeProfiles.contains("security-test"))
            env.getProperty(cookieSecretStr)
        else
            System.getenv()[cookieSecretStr]
        if (cookieSecret.isNullOrBlank())
            throw EnvironmentVariableNotSetException()
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