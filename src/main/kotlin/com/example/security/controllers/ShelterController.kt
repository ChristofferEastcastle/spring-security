package com.example.security.controllers

import com.example.security.models.dtos.AnimalDto
import com.example.security.models.dtos.AnimalRegistrationDto
import com.example.security.models.dtos.AnimalUpdateDto
import com.example.security.models.entities.AnimalEntity
import com.example.security.services.ShelterService
import com.wrongwrong.mapk.core.KMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/shelter")
class ShelterController(
    @Autowired private val shelterService: ShelterService
) {

    @GetMapping("/animals")
    fun retrieveAllAnimals(): ResponseEntity<List<AnimalDto>> {
        val animals = shelterService.retrieveAllAnimals()
            .map { KMapper(::AnimalDto).map(it) }

        return ResponseEntity(animals, HttpStatus.OK)
    }

    @GetMapping("/animals/{id}")
    fun retrieveAnimal(@PathVariable id: Long): ResponseEntity<AnimalEntity> {
        val animal = shelterService.retrieveAnimal(id)
        return when {
            animal.isPresent -> ResponseEntity(animal.get(), HttpStatus.OK)
            animal.isEmpty -> ResponseEntity(HttpStatus.NOT_FOUND)
            else -> {
                ResponseEntity(HttpStatus.BAD_REQUEST)
            }
        }
    }

    @PostMapping("/animals")
    fun registerAnimal(@RequestBody animal: AnimalRegistrationDto): ResponseEntity<AnimalEntity> {
        return ResponseEntity(shelterService.registerAnimal(animal), HttpStatus.CREATED)
    }

    @PutMapping("/animals/{id}")
    fun updateAnimal(@PathVariable id: Long, @RequestBody animal: AnimalUpdateDto): ResponseEntity<Unit> {
        return when (shelterService.updateAnimal(id, animal)) {
            true -> ResponseEntity(HttpStatus.NO_CONTENT)
            false -> ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/animals/{id}")
    fun deleteAnimal(@PathVariable id: Long): ResponseEntity<Unit> {
        return when (shelterService.deleteAnimal(id)) {
            true -> ResponseEntity(HttpStatus.NO_CONTENT)
            false -> ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}