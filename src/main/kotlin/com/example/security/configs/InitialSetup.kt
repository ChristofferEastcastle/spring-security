package com.example.security.configs

import com.example.security.exceptions.AdminEnvPasswordNotFoundException
import com.example.security.exceptions.AdminEnvUsernameNotFoundException
import com.example.security.models.dtos.UserRegistrationDto
import com.example.security.models.entities.AuthorityEntity
import com.example.security.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class InitialSetup(
    @Autowired private val env: Environment,
    @Autowired private val passwordEncoder: BCryptPasswordEncoder

) {

    @Profile("!controller-test")

    fun init(@Autowired userService: UserService) = CommandLineRunner {
        val adminAuthority = userService.createAuthority(AuthorityEntity(name = "ADMIN"))
        val admin = tryGetAdminUser()
        userService.registerUser(admin)
        userService.grantAuthorityToUser(admin.username, adminAuthority.name)
    }


    @Throws(AdminEnvPasswordNotFoundException::class, AdminEnvUsernameNotFoundException::class)
    private fun tryGetAdminUser(): UserRegistrationDto {
        val username: String?
        val password: String?
        if (env.activeProfiles.contains("test")) {
            username = env.getProperty("TEST_USERNAME")
            password = env.getProperty("TEST_PASSWORD")
        } else {
            username = System.getenv()["ADMIN_USERNAME"]
            password = System.getenv()["ADMIN_PASSWORD"]
        }
        if (username == null)
            throw AdminEnvUsernameNotFoundException()
        if (password == null)
            throw AdminEnvPasswordNotFoundException()

        return UserRegistrationDto(
            username,
            passwordEncoder.encode(password)
        )
    }
}

