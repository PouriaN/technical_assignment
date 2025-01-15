package com.lobox.technical_assignment.db.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "title_basic")
data class TitleBasicsEntity(
    @Id
    @Column(name = "tconst", nullable = false, unique = true)
    val tconst: Long,
    val titleType: String,
    val primaryTitle: String,
    val originalTitle: String,
    val isAdult: Boolean,
    val startYear: String,
    val endYear: String?,
    val runtimeMinutes: Int?,
    @ManyToMany
    val genres: List<GenreEntity>,
)
