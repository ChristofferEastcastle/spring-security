package com.example.security.models.dtos

import com.example.security.models.entities.Authorities


data class UserDto(
    val id: Long? = null,
    val username: String,
    val enabled: Boolean,
    val authorities: List<Authorities> = mutableListOf()
)
