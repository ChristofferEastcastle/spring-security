package com.example.security.models.dtos

import com.example.security.models.entities.AnimalType

class AnimalDto(
    val name: String,
    var type: AnimalType,
    val age: Int,
    val health: Int
)