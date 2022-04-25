package com.example.security.controllers

import com.example.security.configs.ControllerTestConfig
import com.example.security.models.dtos.UserDto
import com.example.security.models.entities.UserEntity
import com.example.security.services.UserService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.wrongwrong.mapk.core.KMapper
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get


@WebMvcTest(UserController::class)
@ActiveProfiles("controller-test")
@Import(ControllerTestConfig::class)
class UserControllerTest(@Autowired val userService: UserService) {

    private val userOne = UserDto(id = 1, username = "test1", enabled = true)
    private val userTwo = UserDto(id = 2, username = "test2", enabled = true)
    private val users = listOf(userOne, userTwo)

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val userController: UserController = UserController(userService)

    @Test
    fun testGetUsers() {
        every { userService.fetchAllUsers() } answers {
            users.map { UserEntity(it.id, it.username, password = "not needed", it.enabled) }
        }

        val result = mockMvc.get("/api/users")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andReturn()

        assertThat(result.response.contentAsString)
            .isNotNull
            .isEqualTo(jacksonObjectMapper().writeValueAsString(users))
            .isEqualTo(jacksonObjectMapper().writeValueAsString(userController.getUsers()))
    }

    @Test
    fun testGetOneUser() {
        val userEntity = UserEntity(username = "test", password = "test_password", enabled = true)
        every { userService.getUser(any()) } answers {
            userEntity
        }
        mockMvc.get("/api/users/1")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andExpect { content { json(jacksonObjectMapper()
                .writeValueAsString(KMapper(::UserDto).map(userEntity))) } }

    }
}