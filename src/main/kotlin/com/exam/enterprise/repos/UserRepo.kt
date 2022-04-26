package com.exam.enterprise.repos

import com.exam.enterprise.models.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*


@Repository
interface UserRepo : JpaRepository<UserEntity, Long>{
    fun findByUsername(username: String): UserEntity?

    override fun findById(id: Long): Optional<UserEntity>

    fun existsByUsername(username: String): Boolean
}