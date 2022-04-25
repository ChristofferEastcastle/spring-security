package com.example.security.services

import com.example.security.models.dtos.AnimalDto
import com.example.security.models.dtos.AnimalRegistrationDto
import com.example.security.models.entities.AnimalEntity
import com.example.security.repos.ShelterRepo
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

    fun updateAnimal(id: Long, animal: AnimalDto): Boolean {
        if (shelterRepo.findById(id).isEmpty) return false
        val updatedAnimal = AnimalEntity(
            id,
            animal.name,
            animal.type,
            animal.age,
            animal.health
        )
        shelterRepo.save(updatedAnimal)
        return true
    }
}