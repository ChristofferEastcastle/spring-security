package com.example.security.controllers

import com.example.security.configs.ControllerTestConfig
import com.example.security.security.SecurityConfig
import com.example.security.exceptions.UsernameExistsException
import com.example.security.models.entities.UserEntity
import com.example.security.services.UserService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.wrongwrong.mapk.core.KMapper
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@WebMvcTest(AuthController::class)
@ActiveProfiles("controller-test")
@Import(ControllerTestConfig::class)
class AuthControllerTest(@Autowired private val userService: UserService) {
    @Autowired
    private lateinit var mockMvc: MockMvc

    private val userEntity = UserEntity(
        id = 22, username = "Spiderman", password = "super_hero", enabled = true
    )

    @Test
    fun registerUser() {
        val toRegister = KMapper(com.example.security.models.dtos.UserRegistrationDto::class).map(userEntity)
        every { userService.registerUser(any()) } answers {
            userEntity
        }
        mockMvc.post(SecurityConfig.REGISTER_URL) {
            content = jacksonObjectMapper().writeValueAsString(toRegister)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect { status { isCreated() } }
    }

    @Test
    fun registerUsernameExist() {
        val toRegister = KMapper(com.example.security.models.dtos.UserRegistrationDto::class).map(userEntity)
        every { userService.registerUser(any()) } answers {
            throw UsernameExistsException("Username does already exist")
        }
        mockMvc.post(SecurityConfig.REGISTER_URL) {
            content = jacksonObjectMapper().writeValueAsString(toRegister)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect { status { isBadRequest() } }
            .andExpect {
                content {
                    json(
                        jacksonObjectMapper().writeValueAsString(
                            mapOf("error_message" to "Username does already exist")
                        )
                    )
                }
            }
    }
}