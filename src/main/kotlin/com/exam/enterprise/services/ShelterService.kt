package com.exam.enterprise.services

import com.exam.enterprise.models.dtos.AnimalRegistrationDto
import com.exam.enterprise.models.dtos.AnimalUpdateDto
import com.exam.enterprise.models.entities.AnimalEntity
import com.exam.enterprise.repos.ShelterRepo
import com.wrongwrong.mapk.core.KMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ShelterService(
    @Autowired private val shelterRepo: ShelterRepo,
) {
    fun retrieveAllAnimals(): List<AnimalEntity> {
        return shelterRepo.findAll()
    }

    fun registerAnimal(animal: AnimalRegistrationDto): AnimalEntity {
        return shelterRepo.save(
            AnimalEntity(
                name = animal.name,
                type = animal.type,
                age = animal.age,
                health = animal.health
            )
        )
    }

    fun retrieveAnimal(id: Long): Optional<AnimalEntity> {
        return shelterRepo.findById(id)
    }

    fun updateAnimal(id: Long, animal: AnimalUpdateDto): Boolean {
        if (shelterRepo.findById(id).isEmpty) return false

        val updatedAnimal = KMapper(::AnimalEntity)
            .map(animal, mapOf("id" to id))
        shelterRepo.save(updatedAnimal)
        return true
    }

    // Returning true if the entity was found, and we can safely delete.
    // Returning false if the entity was not found, telling controller we could not delete.
    fun deleteAnimal(id: Long): Boolean {
        val animalFound = shelterRepo.findById(id)
        if (animalFound.isEmpty) return false

        shelterRepo.deleteById(id)
        return true
    }
}