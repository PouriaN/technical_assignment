package com.lobox.technical_assignment.db.entities

import com.lobox.technical_assignment.exceptions.ImportCsvException
import com.lobox.technical_assignment.util.convertToNullWhenEmpty
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
    val primaryName: String,
    val birthYear: String,
    val deathYear: String?,
) {
    companion object {
        fun of(csvLine: Array<String>) = NameBasicsEntity(
            nconst = csvLine[0].convertToNullWhenEmpty() ?: throw ImportCsvException(field = "nconst", line = csvLine),
            primaryName = csvLine[1].convertToNullWhenEmpty() ?: throw ImportCsvException(field = "primaryName", line = csvLine),
            birthYear = csvLine[2].convertToNullWhenEmpty() ?: throw ImportCsvException(field = "birthYear", line = csvLine),
            deathYear = csvLine[3].convertToNullWhenEmpty(),
        )
    }
}
