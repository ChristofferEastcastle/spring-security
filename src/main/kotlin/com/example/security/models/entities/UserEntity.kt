package com.example.security.models.entities

import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,
    val username: String,
    val password: String,
    val enabled: Boolean,

    @OneToMany(fetch = FetchType.EAGER)
    val authorities: MutableList<AuthorityEntity> = mutableListOf()
)