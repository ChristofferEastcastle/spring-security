package com.exam.enterprise.models.dtos

import com.exam.enterprise.models.entities.AuthorityEntity


data class UserDto(
    val id: Long? = null,
    val username: String,
    val enabled: Boolean,
    val authorities: List<AuthorityEntity> = mutableListOf()
)
