package com.exam.enterprise.controllers

import com.exam.enterprise.configs.ControllerTestConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest
@ActiveProfiles("controller-test")
@Import(ControllerTestConfig::class)
class HelloControllerTest{

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun helloTest() {
        mockMvc.get("/")
            .andExpect { status { isOk() } }
            .andExpect { content { string("Hello!") } }
    }
}