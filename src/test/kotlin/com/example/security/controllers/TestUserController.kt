package com.example.security.controllers

import com.example.security.models.dtos.UserDto
import com.example.security.models.entities.UserEntity
import com.example.security.repos.UserRepo
import com.example.security.services.UserService
import com.fasterxml.jackson.module.kotlin.asUShort
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.event.annotation.BeforeTestClass
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.*

@ExtendWith(SpringExtension::class)
@WebMvcTest
@Import(UserService::class)
@Profile("test")
@AutoConfigureMockMvc(addFilters = false)
class TestUserController {

    private val userOne = UserDto(id = 1, username = "test1", enabled = true)
    private val userTwo = UserDto(id = 2, username = "test2", enabled = true)
    private val users = listOf(userOne, userTwo)


    @Autowired
    private lateinit var mockMvc: MockMvc

    private val userService = mockk<UserService>()
    private val userRepo = mockk<UserRepo>()

    /*@TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun getMockMvc(): MockMvc {

        }
    }*/



    @Test
    fun testGetUsers() {
        /*every { userService.fetchAllUsers() } answers  {
            users
        }*/

        val result = mockMvc.get("/api/users")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andReturn()

        assertThat(result.response.contentAsString)
            .isEqualTo(jacksonObjectMapper().writeValueAsString(users))
    }

    @Test
    fun testGetOneUser() {

        mockMvc.get("/api/users/1")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            //.andExpect { content { json(jacksonObjectMapper().writeValueAsString(userEntity)) } }

    }
}