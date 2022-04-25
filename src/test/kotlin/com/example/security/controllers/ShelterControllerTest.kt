package com.example.security.controllers

import com.example.security.configs.ControllerTestConfig
import com.example.security.models.dtos.AnimalDto
import com.example.security.models.entities.AnimalEntity
import com.example.security.models.entities.AnimalType
import com.example.security.services.ShelterService
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

@WebMvcTest
@ActiveProfiles("controller-test")
@Import(ControllerTestConfig::class)
class ShelterControllerTest(
    @Autowired private val shelterService: ShelterService
) {
    @Autowired
    private lateinit var mockMvc: MockMvc
    private val shelterController = ShelterController(shelterService)

    private val testAnimal1 = AnimalEntity(1, name = "Batman", AnimalType.LION, 45, 99)
    private val testAnimal2 = AnimalEntity(2, name = "Catwoman", AnimalType.TIGER, 27, 100 * 9)

    @Test
    fun retrieveAllAnimals() {
        every { shelterService.retrieveAllAnimals() } answers {
            listOf(testAnimal1, testAnimal2)
        }

        val result = mockMvc.get("/api/shelter")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andReturn()

        // Mapping list of entities to dtos for easy checking of the response.
        val dtoList = listOf(testAnimal1, testAnimal2)
            .map { KMapper(::AnimalDto).map(it) }

        val mapper = jacksonObjectMapper()

        assertThat(result.response.contentAsString)
            .isEqualTo(mapper.writeValueAsString(dtoList))
            .isEqualTo(mapper.writeValueAsString(shelterController.retrieveAllAnimals().body))
    }

    @Test
    fun retrieveAnimal() {

    }

    @Test
    fun registerAnimal() {
    }

    @Test
    fun updateAnimal() {
    }

    @Test
    fun deleteAnimal() {
    }
}