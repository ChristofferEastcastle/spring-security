package com.example.security.integration

import com.example.security.models.dtos.UserRegistrationDto
import com.example.security.security.SecurityConfig.Companion.LOGIN_URL
import com.example.security.security.SecurityConfig.Companion.REGISTER_URL
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class EndToEndTest {
    @Autowired
    private lateinit var mockMvc: MockMvc
    private val jsonMapper = jacksonObjectMapper()

    @Test
    fun registerUser() {
        val toRegister = UserRegistrationDto(
            username = "the-joker",
            password = "batmans-bestfriend"
        )
        // REGISTERING NEW USER
        val result = mockMvc.post(REGISTER_URL) {
            contentType = APPLICATION_JSON
            content = jsonMapper.writeValueAsString(toRegister)
        }
            .andExpect { status { isCreated() } }
            .andReturn().response
        assertThat(result.contentAsString)
            .isNotNull

        // LOGGING IN AS NEW USER
        mockMvc.post(LOGIN_URL) {
            contentType = APPLICATION_FORM_URLENCODED
            param("username", toRegister.username)
            param("password", toRegister.password)
        }
            .andExpect { status { isFound() } }
            .andExpect { cookie { exists("access_token") } }
    }
}