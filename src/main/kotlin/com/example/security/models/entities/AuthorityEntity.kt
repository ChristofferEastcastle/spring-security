package com.example.security.models.entities

import javax.persistence.*
import javax.persistence.EnumType.STRING
import javax.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "authorities")
class AuthorityEntity(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    @Enumerated(STRING)
    val name: AuthorityName
)

enum class AuthorityName {
    ADMIN, USER, TRAINEE
}