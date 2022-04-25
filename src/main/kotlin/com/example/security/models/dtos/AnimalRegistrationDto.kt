package com.example.security.models.dtos

import com.example.security.models.entities.AnimalType

class AnimalRegistrationDto(
    val name: String,
    var type: AnimalType,
    val age: Int,
    val health: Int
)