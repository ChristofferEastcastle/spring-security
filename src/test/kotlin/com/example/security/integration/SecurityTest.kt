package com.example.security.integration

import com.example.security.utils.JwtUtil
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import javax.servlet.http.Cookie

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SecurityTest(
    @Autowired
    private var env: Environment
) {



    @Autowired
    private lateinit var mockMvc: MockMvc

    private val username: String = env.getProperty("TEST_USERNAME")!!
    private val password: String = env.getProperty("TEST_PASSWORD")!!

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
            .isEqualTo(UNAUTHORIZED.value())
    }

    @Test
    fun basicLoginTest() {

        val result = mockMvc.perform(
            post("/login")
                .contentType(APPLICATION_FORM_URLENCODED)
                .param("username", username)
                .param("password", password)
        ).andReturn()

        val accessToken = result.response.getCookie("access_token")
        assertThat(accessToken)
            .isNotNull

        mockMvc.get("/api/users") {
            cookie(accessToken!!)
            accept = APPLICATION_JSON
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
    }

    @Test
    fun invalidUserTokenTest() {
        val token = JwtUtil.createToken(
            UserDetailsCreator().loadUserByUsername("test") as User,
            issuer = "tester.com", minutes = 10
        )

        val errorMessage = "{\"error_message\":\"Error authenticating user\"}"
        mockMvc.get("/") {
            cookie(Cookie("access_token", token))
        }
            .andExpect { status { isUnauthorized() } }
            .andExpect { content { string(errorMessage) } }

    }

    @Test
    fun corruptTokenTest() {
        val token = JwtUtil.createToken(
            UserDetailsCreator().loadUserByUsername(username) as User,
            issuer = "tester.com", minutes = 0
        ).substring(2)

        val errorMessage = jacksonObjectMapper().writeValueAsString(mapOf("error_message" to "access token invalid!"))
        mockMvc.get("/") {
            cookie(Cookie("access_token", token))
        }
            .andExpect { status { isUnauthorized() } }
            .andExpect { content { string(errorMessage) } }
    }
    // Using this class to create a fake JWT token to test security
    @Service
    class UserDetailsCreator : UserDetailsService {
        override fun loadUserByUsername(username: String): UserDetails {
            return User(
                username,
                "test_password",
                mutableListOf(SimpleGrantedAuthority("ADMIN"))
            )
        }
    }
}