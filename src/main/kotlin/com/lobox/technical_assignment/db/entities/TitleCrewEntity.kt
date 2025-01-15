package com.lobox.technical_assignment.db.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "title_crew")
data class TitleCrewEntity(
    @Id
    @Column(name = "tconst", nullable = false, unique = true)
    val tconst: Long,
    val directors: String?,
    val writers: String?,
)
