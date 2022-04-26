package com.exam.enterprise.models.entities

import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "authorities")
class AuthorityEntity(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    val name: String
)

enum class Authorities {
    ADMIN, USER, TRAINEE
}