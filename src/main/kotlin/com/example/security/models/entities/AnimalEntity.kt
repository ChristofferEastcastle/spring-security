package com.example.security.models.entities

import javax.persistence.*
import javax.persistence.EnumType.STRING
import javax.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "animals")
class AnimalEntity(
    @Id
    @SequenceGenerator(name = "animals_id_seq")
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,
    val name: String,
    @Enumerated(STRING)
    var type: AnimalType,
    val age: Int,
    val health: Int
)

enum class AnimalType {
    TIGER, LION, GIRAFFE, HEDGEHOG, PIG, MEERKAT, BEAR
}