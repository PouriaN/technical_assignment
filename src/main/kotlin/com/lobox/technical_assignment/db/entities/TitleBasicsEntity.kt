package com.lobox.technical_assignment.db.entities

import com.lobox.technical_assignment.exceptions.ImportCsvException
import com.lobox.technical_assignment.util.convertToNullWhenEmpty
import com.lobox.technical_assignment.util.toBool
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcType
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "title_basic")
data class TitleBasicsEntity(
    @Id
    @Column(name = "tconst", nullable = false, unique = true)
    val tconst: String,
    val titleType: String,
    @Column(name = "primary_title", length = 500)
    val primaryTitle: String,
    @Column(name = "original_title", length = 500)
    val originalTitle: String,
    val isAdult: Boolean,
    val startYear: String?,
    val endYear: String?,
    val runtimeMinutes: Int?,
    @ManyToMany
    val genres: List<GenreEntity>,
)