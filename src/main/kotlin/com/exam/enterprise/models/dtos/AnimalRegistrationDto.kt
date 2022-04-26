package com.exam.enterprise.models.dtos

import com.exam.enterprise.models.entities.AnimalType

class AnimalRegistrationDto(
    val name: String,
    var type: AnimalType,
    val age: Int,
    val health: Int
)