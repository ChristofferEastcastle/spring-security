package com.exam.enterprise.services

import com.exam.enterprise.models.dtos.AnimalRegistrationDto
import com.exam.enterprise.models.dtos.AnimalUpdateDto
import com.exam.enterprise.models.entities.AnimalEntity
import com.exam.enterprise.models.entities.AnimalType.*
import com.exam.enterprise.models.entities.UserEntity
import com.exam.enterprise.repos.ShelterRepo
import com.wrongwrong.mapk.core.KMapper
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

internal class ShelterServiceTest {
    private val shelterRepo = mockk<ShelterRepo>()
    private val shelterService = ShelterService(shelterRepo)

    private val testAnimal1 = AnimalEntity(1, name = "Test1", LION, 45, 99)
    private val testAnimal2 = AnimalEntity(2, name = "Test2", GIRAFFE, 88, 21)

    @Test
    fun retrieveAllAnimals() {
        every { shelterRepo.findAll() } answers {
            listOf(testAnimal1, testAnimal2)
        }
        val animals = shelterService.retrieveAllAnimals()
        assertThat(animals)
            .isNotNull
            .isEqualTo(listOf(testAnimal1, testAnimal2))
    }

    @Test
    fun registerAnimal() {
        /*val animalToRegister = AnimalRegistrationDto(
            name = "Batman",
            type = AnimalType.GIRAFFE,
            age = 68,
            health = 299
        )*/
        val animalToRegister = KMapper(::AnimalRegistrationDto)
            .map(testAnimal1)

        every { shelterRepo.save(any()) } answers {
            testAnimal1
        }
        val actualRegistered = shelterService.registerAnimal(animalToRegister)
        assertThat(actualRegistered)
            .isNotNull
            .hasNoNullFieldsOrProperties()
            .isEqualTo(testAnimal1)
    }

    @Test
    fun retrieveAnimal() {
        every { shelterRepo.findById(any()) } answers {
            Optional.of(testAnimal1)
        }
        assertThat(shelterService.retrieveAnimal(1))
            .isNotNull
            .isNotEmpty
            .isEqualTo(Optional.of(testAnimal1))
    }

    // Basically swapping all the fields from testAnimal1 to testAnimal2
    @Test
    fun updateAnimal() {
        every { shelterRepo.findById(2) } answers {
            Optional.of(testAnimal2)
        }

        every { shelterRepo.save(any()) } answers {
            testAnimal1.apply {
                id = testAnimal2.id
            }
        }

        val updated = KMapper(::AnimalUpdateDto).map(testAnimal1)
        val result = shelterService.updateAnimal(2, updated)

        assertThat(result)
            .isTrue
    }

    @Test
    fun noAnimalFound() {
        every { shelterRepo.findById(1) } answers {
            Optional.empty()
        }
        val animal = shelterService.retrieveAnimal(1)

        assertThat(animal)
            .isEqualTo(Optional.empty<UserEntity>())
    }
}