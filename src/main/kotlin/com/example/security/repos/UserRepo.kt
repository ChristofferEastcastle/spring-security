package com.example.security.repos

import com.example.security.models.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UserRepo : JpaRepository<UserEntity, Long>{
    fun findByUsername(username: String?): UserEntity
}