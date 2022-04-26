package com.exam.enterprise.models.entities

import javax.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,
    @Column(unique = true)
    val username: String,
    val password: String,
    val enabled: Boolean,
) {
    @ManyToMany(fetch = FetchType.EAGER)
    val authorities: MutableList<AuthorityEntity> = mutableListOf()
}