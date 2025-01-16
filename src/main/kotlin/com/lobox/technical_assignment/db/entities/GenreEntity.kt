package com.lobox.technical_assignment.db.entities

import jakarta.persistence.*

@Entity
@Table(name = "genre")
class GenreEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long? = null,
    val name: String,
)