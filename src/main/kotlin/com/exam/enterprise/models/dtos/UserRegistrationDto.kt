package com.exam.enterprise.models.dtos

import java.io.Serializable

data class UserRegistrationDto(val username: String, val password: String) : Serializable
