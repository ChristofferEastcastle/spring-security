package com.example.security.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SecurityTest(
) {
    @Autowired
    private lateinit var mockMvc: MockMvc



    @Test
    fun failBasicLoginTest() {
        val username = "not_found"
        val password = "no_password"

        val result = mockMvc.perform(
            post("/login")
                .contentType(APPLICATION_FORM_URLENCODED)
                .param("username", username)
                .param("password", password)
        )
            .andReturn()
        assertThat(result.response.status)
            .isEqualTo(FORBIDDEN.value())
    }

    @Test
    fun basicLoginTest() {
        val username = "joe@bob.com"
        val password = "pirate"

        mockMvc.post("/login") {
            //contentType(APPLICATION_FORM_URLENCODED)
        }
    }
}