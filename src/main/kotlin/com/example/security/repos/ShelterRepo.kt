package com.example.security.repos

import com.example.security.models.entities.AnimalEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository

interface ShelterRepo : JpaRepository<AnimalEntity, Long> {
    fun deleteAnimalEntityById(id: Long): Boolean
}