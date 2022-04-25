package com.example.security.models.entities

import javax.persistence.*

@Entity
@Table(name = "animals")
class AnimalEntity(
    @Id
    @SequenceGenerator(name = "animals_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,
    val name: String,
    @Enumerated(EnumType.ORDINAL)
    var type: AnimalType,
    val age: Int,
    val health: Int
)

enum class AnimalType {
    TIGER, LION, GIRAFFE, HEDGEHOG
}