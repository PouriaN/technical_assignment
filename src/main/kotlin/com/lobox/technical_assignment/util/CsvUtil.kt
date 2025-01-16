package com.lobox.technical_assignment.util

import com.lobox.technical_assignment.db.entities.NameBasicsEntity
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import java.nio.file.Files
import java.nio.file.Path

fun String.convertToNullWhenEmpty() = if(this == "\\N") null else this
fun readCsv(filePath: String, lineReader: (line: Array<String>) -> Any) {
    Files.newBufferedReader(Path.of(filePath)).use { reader ->
        CSVReaderBuilder(reader)
            .withCSVParser(CSVParserBuilder().withSeparator('\t').build())
            .withSkipLines(1)
            .build()
            .use { csvReader ->
                val line = csvReader.readNext()
                while (line != null) {
                    lineReader(line)
                }
            }
    }
}