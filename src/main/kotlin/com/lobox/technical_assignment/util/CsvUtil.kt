package com.lobox.technical_assignment.util

import java.nio.file.Files
import java.nio.file.Path

fun String.convertToNullWhenEmpty() = if (this == "\\N") null else this

fun readCsv(
    directory: String,
    fileName: String,
    skipLines: Int = 1,
    delimiter: Char = '\t',
    lineReader: (line: List<String>) -> Unit
) = Files.newBufferedReader(Path.of(directory, fileName)).use { fileReader ->
    (1..skipLines).forEach { _ -> fileReader.readLine() }
    var line = fileReader.readLine()
    while (line != null) {
        val csvLine = line.split(delimiter)
        lineReader(csvLine)
        line = fileReader.readLine()
    }
}
