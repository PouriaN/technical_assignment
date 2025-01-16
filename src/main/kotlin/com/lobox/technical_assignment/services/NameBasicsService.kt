package com.lobox.technical_assignment.services

import com.lobox.technical_assignment.db.repositories.NameBasicsRepository
import com.lobox.technical_assignment.exceptions.ImportException
import com.lobox.technical_assignment.util.convertToNullWhenEmpty
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path

@Service
class NameBasicsService(
    val nameBasicsRepository: NameBasicsRepository,
    @Value("\${com.lobox.technical_assignment.csv_path}")
    val datasetPath: String,
    @Value("\${com.lobox.technical_assignment.batch_size}")
    val batchSize: Int,
    val jdbcTemplate: JdbcTemplate,
) : DataInitializeServiceInterface {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)

    companion object {
        private const val csvFileName = "name.basics.tsv"
        private const val query = "INSERT INTO name_basic (nconst, alive) VALUES (?, ?)"
    }

    override fun initialize() {
        val start = System.currentTimeMillis()
        try {
            logger.info("initializing has been started NameBasicsService")

            var csvLines = mutableListOf<List<String>>()
            Files.newBufferedReader(Path.of(datasetPath, csvFileName)).use { fileReader ->
                fileReader.readLine()
                var line = fileReader.readLine()
                while (line != null) {
                    val csvLine = line.split('\t')
                    csvLines.add(csvLine)
                    if (csvLines.size > batchSize) {
                        batchInsert(csvLines)
                        csvLines = mutableListOf()
                    }
                    line = fileReader.readLine()
                }
            }
            batchInsert(csvLines)
            logger.info("NameBasicsService finished in ${System.currentTimeMillis() - start}ms")

        } catch (e: Exception) {
            logger.error("NameBasicsService finished in ${System.currentTimeMillis() - start}ms", e)
        }
    }

    fun batchInsert(csvLines: MutableList<List<String>>) {
        jdbcTemplate.batchUpdate(query, csvLines, csvLines.size) { ps, seperatedColumns ->
            ps.setString(
                1,
                seperatedColumns[0].convertToNullWhenEmpty() ?: throw ImportException(
                    field = "nconst",
                    line = seperatedColumns
                )
            )
            ps.setBoolean(2, seperatedColumns[3].convertToNullWhenEmpty().isNullOrEmpty())
        }
    }

    fun getTopTen() = nameBasicsRepository.findAll(Pageable.ofSize(10))
}