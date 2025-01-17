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

data class TitleBasicsEntity(
    val tconst: String,
    val titleType: String,
    val primaryTitle: String,
    val originalTitle: String,
    val isAdult: Boolean,
    val startYear: String?,
    val endYear: String?,
    val runtimeMinutes: Int?,
    val genres: List<String>,
) {
    companion object {
        fun of(values: List<String>) = TitleBasicsEntity(
            tconst = values[0].convertToNullWhenEmpty() ?: throw Exception("import exception"),
            titleType = values[1].convertToNullWhenEmpty() ?: throw Exception("import exception"),
            primaryTitle = values[2].convertToNullWhenEmpty() ?: throw Exception("import exception"),
            originalTitle = values[3].convertToNullWhenEmpty() ?: throw Exception("import exception"),
            isAdult = values[4].convertToNullWhenEmpty()?.toBool() ?: throw Exception("import exception"),
            startYear = values[5].convertToNullWhenEmpty(),
            endYear = values[6].convertToNullWhenEmpty(),
            runtimeMinutes = values[7].convertToNullWhenEmpty()?.toInt(),
            genres = values[8].convertToNullWhenEmpty()?.split(',') ?: emptyList(),
        )
    }
}