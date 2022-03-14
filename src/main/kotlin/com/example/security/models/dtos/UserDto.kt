package com.example.security.models.dtos

import java.io.Serializable

data class UserDto(val id: Long? = null, val username: String? = null, val enabled: Boolean? = null) : Serializable
