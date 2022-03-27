package com.example.security.services

import com.example.security.models.dtos.UserRegistrationDto
import com.example.security.models.entities.AuthorityEntity
import com.example.security.models.entities.UserEntity
import com.example.security.repos.AuthorityRepo
import com.example.security.repos.UserRepo
import com.example.security.configs.SecurityConfig.Authorities.USER
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException

internal class UserServiceTest {

    private val userRepo = mockk<UserRepo>()
    private val authorityRepo = mockk<AuthorityRepo>()
    private val userService = UserService(userRepo, authorityRepo)


    @Test
    fun fetchAllUsers() {
        val userOne = UserEntity(id = 1, username = "test1", password = "password1", enabled = true)
        val userTwo = UserEntity(id = 2, username = "test2", password = "password2", enabled = true)
        every { userRepo.findAll() } answers {
            listOf(userOne, userTwo)
        }

        assertThat(userService.fetchAllUsers()).isEqualTo(listOf(userOne, userTwo))
    }

    @Test
    fun loadUserByUsername() {
        val username = "Best Username Ever"
        every { userRepo.findByUsername(username) } answers {
            UserEntity(id = 1, username, password = "bestpassword123", enabled = true)
        }

        assertThat(userService.loadUserByUsername(username))
            .isNotNull
            .hasFieldOrPropertyWithValue("username", username)
    }

    @Test
    fun errorLoadingUserByUsername() {
        val username = "The Joker"
        every { userRepo.findByUsername(username) } answers {
            throw Exception()
        }

        assertThatExceptionOfType(UsernameNotFoundException::class.java)
            .isThrownBy { userService.loadUserByUsername(username) }
            .withMessage("Error authenticating user")
    }

    @Test
    fun registerUser() {
        val userToSave = UserRegistrationDto(
            username = "Batman",
            password = "ilikecatwoman123"
        )
        every { userRepo.save(any()) } answers {
            UserEntity(id = 2, username = userToSave.username, password = userToSave.password, enabled = true)
        }
        every { authorityRepo.findByName(any()) } answers {
            AuthorityEntity(name = USER.name)
        }
        val registered = userService.registerUser(userToSave)

        assertThat(registered)
            .isNotNull
            .hasFieldOrPropertyWithValue("username", userToSave.username)
            .hasFieldOrPropertyWithValue("password", userToSave.password)
            .hasNoNullFieldsOrProperties()
    }

    @Test
    fun createAuthority() {
        val authority = AuthorityEntity(id = 1, name = "SUPER_ADMIN")
        every { authorityRepo.save(any()) } answers {
            authority
        }
        val createdAuthority = userService.createAuthority(authority)

        assertThat(createdAuthority)
            .isNotNull
            .hasNoNullFieldsOrProperties()
            .isEqualTo(authority)
    }

    @Test
    fun grantAuthorityToUser() {
        val username = "Iron Man"
        val authorityString = "ULTRA_ADMIN"
        val userEntity = UserEntity(id = 1, username, password = "Black Widow;)", enabled = true)
        every { userRepo.findByUsername(username) } answers {
            userEntity
        }

        every { userRepo.save(userEntity) } answers {
            userEntity
        }

        every { authorityRepo.findByName(authorityString) } answers {
            AuthorityEntity(id = 1, authorityString)
        }

        userService.grantAuthorityToUser(username, authorityString)

        val userAuthorities = userService.loadUserByUsername(username).authorities
        assertThat(userAuthorities)
            .isNotNull
            .contains(SimpleGrantedAuthority(authorityString))
    }
}