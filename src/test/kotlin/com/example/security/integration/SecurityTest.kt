package com.example.security.integration

import com.example.security.services.UserService
import com.example.security.utils.JwtUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
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
class SecurityTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userService: UserService

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
        val username = "joe@bob.com"
        val password = "pirate"

        val result = mockMvc.perform(
            post("http://localhost:8080/login")
                .contentType(APPLICATION_FORM_URLENCODED)
                .param("username", username)
                .param("password", password)
        ).andReturn()

        val accessToken = result.response.getCookie("access_token")
        assertThat(accessToken)
            .isNotNull

        mockMvc.get("http://localhost:8080/api/users") {
            cookie(accessToken!!)
            accept = APPLICATION_JSON
        }
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
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

    @Test
    fun invalidTokenTest() {
        val token = JwtUtil.createToken(
            UserDetailsCreator().loadUserByUsername("test") as User,
            "tester.com", 10
        )

        val errorMessage = "{\"error_message\":\"Error authenticating user\"}"
        mockMvc.get("/") {
            cookie(Cookie("access_token", token))
        }
            .andExpect { status { isUnauthorized() } }
            .andExpect { content { string(errorMessage) } }

    }
}