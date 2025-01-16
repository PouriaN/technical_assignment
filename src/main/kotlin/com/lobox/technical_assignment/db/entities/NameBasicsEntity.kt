package com.lobox.technical_assignment.db.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "name_basic")
data class NameBasicsEntity(
    @Id
    @Column(name = "nconst", nullable = false, unique = true)
    val nconst: String,
    val alive: Boolean,
)
