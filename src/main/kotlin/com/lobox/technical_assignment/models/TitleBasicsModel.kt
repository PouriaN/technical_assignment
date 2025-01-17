package com.lobox.technical_assignment.models

import com.lobox.technical_assignment.exceptions.ImportException
import com.lobox.technical_assignment.util.convertToNullWhenEmpty
import com.lobox.technical_assignment.util.toBool

data class TitleBasicsModel(
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
        fun of(values: List<String>) = TitleBasicsModel(
            tconst = values[0].convertToNullWhenEmpty() ?: throw ImportException("tconst"),
            titleType = values[1].convertToNullWhenEmpty() ?: throw ImportException("titleType"),
            primaryTitle = values[2].convertToNullWhenEmpty() ?: throw ImportException("primaryTitle"),
            originalTitle = values[3].convertToNullWhenEmpty() ?: throw ImportException("originalTitle"),
            isAdult = values[4].convertToNullWhenEmpty()?.toBool() ?: throw ImportException("isAdult"),
            startYear = values[5].convertToNullWhenEmpty(),
            endYear = values[6].convertToNullWhenEmpty(),
            runtimeMinutes = values[7].convertToNullWhenEmpty()?.toInt(),
            genres = values[8].convertToNullWhenEmpty()?.split(',') ?: emptyList(),
        )
    }
}