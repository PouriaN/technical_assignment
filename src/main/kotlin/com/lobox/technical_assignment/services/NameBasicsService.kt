package com.lobox.technical_assignment.services

import com.lobox.technical_assignment.db.entities.NameBasicsEntity
import com.lobox.technical_assignment.db.repositories.NameBasicsRepository
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Files
import java.nio.file.Path

@Service
class NameBasicsService(
    val nameBasicsRepository: NameBasicsRepository,
    @Value("\${com.lobox.technical_assignment.csv_path}")
    val datasetPath: String,
) {
    companion object {
        private const val csvFileName = "name.basics.tsv"
    }

    @PostConstruct
    @Transactional
    fun importData() {
        Files.newBufferedReader(Path.of(datasetPath, csvFileName)).use { reader ->
            CSVReaderBuilder(reader)
                .withCSVParser(CSVParserBuilder().withSeparator('\t').build())
                .build()
                .use { csvReader ->
                    var names = mutableListOf<NameBasicsEntity>()
                    while (csvReader.readNext().also { names.add(NameBasicsEntity.of(it)) } != null) {
                        if (names.size == 1000) {
                            nameBasicsRepository.saveAllAndFlush(names)
                            names = mutableListOf()
                            break
                        }
                    }
                }
        }
    }

    fun getTopTen() = nameBasicsRepository.findAll(Pageable.ofSize(10))
}