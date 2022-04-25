package com.example.security.controllers

import com.example.security.configs.ControllerTestConfig
import com.example.security.models.dtos.AnimalDto
import com.example.security.models.dtos.AnimalRegistrationDto
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
import org.springframework.test.web.servlet.*
import java.util.*

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
    private val mapper = jacksonObjectMapper()

    private val path = "/api/shelter"

    @Test
    fun retrieveAllAnimals() {
        every { shelterService.retrieveAllAnimals() } answers {
            listOf(testAnimal1, testAnimal2)
        }

        val result = mockMvc.get(path)
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andReturn()

        // Mapping list of entities to dtos for easy checking of the response.
        val dtoList = listOf(testAnimal1, testAnimal2)
            .map { KMapper(::AnimalDto).map(it) }


        assertThat(result.response.contentAsString)
            .isEqualTo(mapper.writeValueAsString(dtoList))
            .isEqualTo(mapper.writeValueAsString(shelterController.retrieveAllAnimals().body))
    }

    @Test
    fun retrieveAnimal() {
        val id: Long = 1
        every { shelterService.retrieveAnimal(id) } answers {
            Optional.of(testAnimal1)
        }
        val result = mockMvc.get("$path/$id")
            .andExpect { status { isOk() } }
            .andExpect { content { contentType(APPLICATION_JSON) } }
            .andReturn()

        assertThat(result.response.contentAsString)
            .isEqualTo(mapper.writeValueAsString(testAnimal1))
            .isEqualTo(mapper.writeValueAsString(shelterController.retrieveAnimal(id).body))
    }

    @Test
    fun animalNotFound() {
        val id: Long = 666
        every { shelterService.retrieveAnimal(id) } answers {
            Optional.empty()
        }
        mockMvc.get("$path/$id")
            .andExpect { status { isNotFound() } }
    }

    @Test
    fun registerAnimal() {
        val animalToRegister = KMapper(::AnimalRegistrationDto).map(testAnimal2)
        every { shelterService.registerAnimal(any()) } answers {
            testAnimal2
        }

        mockMvc.post(path) {
            contentType = APPLICATION_JSON
            content = mapper.writeValueAsString(animalToRegister)
        }
            .andExpect { status { isCreated() } }
            .andExpect { content { json(mapper.writeValueAsString(testAnimal2)) } }
            .andExpect { content { json(mapper.writeValueAsString(shelterController.registerAnimal(animalToRegister).body)) } }
    }

    @Test
    fun updateAnimal() {
        val id: Long = 1
        val animalToUpdate = KMapper(::AnimalDto).map(testAnimal1)
        every { shelterService.updateAnimal(any(), any()) } answers {
            true
        }
        mockMvc.put("$path/$id") {
            contentType = APPLICATION_JSON
            content = mapper.writeValueAsString(animalToUpdate)
        }
            .andExpect { status { isNoContent() } }
    }

    @Test
    fun updateAnimalNotFound() {
        val id: Long = 1
        val animalToUpdate = KMapper(::AnimalDto).map(testAnimal1)
        every { shelterService.updateAnimal(any(), any()) } answers {
            false
        }
        mockMvc.put("$path/$id") {
            contentType = APPLICATION_JSON
            content = mapper.writeValueAsString(animalToUpdate)
        }
            .andExpect { status { isNotFound() } }
    }

    @Test
    fun deleteAnimal() {
        val id: Long = 1
        every { shelterService.deleteAnimal(id) } answers {
            true
        }
        mockMvc.delete("$path/$id")
            .andExpect { status { isNoContent() } }
    }

    @Test
    fun deleteAnimalNotFound() {
        val id: Long = 1
        every { shelterService.deleteAnimal(id) } answers {
            false
        }
        mockMvc.delete("$path/$id")
            .andExpect { status { isNotFound() } }
    }
}