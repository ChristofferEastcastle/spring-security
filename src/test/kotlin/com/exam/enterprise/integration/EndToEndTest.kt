package com.exam.enterprise.integration

import com.exam.enterprise.models.dtos.AnimalDto
import com.exam.enterprise.models.dtos.AnimalRegistrationDto
import com.exam.enterprise.models.dtos.UserRegistrationDto
import com.exam.enterprise.models.entities.AnimalType.HEDGEHOG
import com.exam.enterprise.security.SecurityConfig.Companion.LOGIN_URL
import com.exam.enterprise.security.SecurityConfig.Companion.REGISTER_URL
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.wrongwrong.mapk.core.KMapper
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
    // This test is also confirming that regular user can POST new animal to shelter.
    @Test
    fun addNewAnimalToShelter() {
        // LOGGING IN
        val loginResult = mockMvc.post(LOGIN_URL) {
            contentType = APPLICATION_FORM_URLENCODED
            param("username", "regular_user")
            param("password", "best_password")
        }.andExpect { cookie { exists("access_token") } }
            .andReturn()
        // GETTING COOKIE FROM LOGIN
        val cookie = loginResult.response.getCookie("access_token")
        assertThat(cookie).isNotNull
        // POSTING NEW ANIMAL
        val animalToAdd = AnimalRegistrationDto("Flix", HEDGEHOG, 3, 78)
        val postResult = mockMvc.post("/api/shelter/animals") {
            contentType = APPLICATION_JSON
            content = jsonMapper.writeValueAsString(animalToAdd)
            cookie(cookie!!)
        }
            .andExpect { status { isCreated() } }
            .andReturn()
        // CHECKING THAT RETURN RESULT IS CORRECT
        val animalDto = jsonMapper
            .readValue(postResult.response.contentAsString, AnimalDto::class.java)
        assertThat(animalDto)
            .isNotNull
            .isEqualTo(KMapper(::AnimalDto)
                .map(animalToAdd, mapOf("id" to animalDto.id)))
    }
}