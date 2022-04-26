package com.exam.enterprise.repos

import com.exam.enterprise.models.entities.AnimalEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository

interface ShelterRepo : JpaRepository<AnimalEntity, Long>