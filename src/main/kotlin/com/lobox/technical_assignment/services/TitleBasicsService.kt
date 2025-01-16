package com.lobox.technical_assignment.services

import com.lobox.technical_assignment.db.entities.GenreEntity
import com.lobox.technical_assignment.db.repositories.GenreRepository
import com.lobox.technical_assignment.db.repositories.TitleBasicsRepository
import com.lobox.technical_assignment.exceptions.ImportException
import com.lobox.technical_assignment.util.convertToNullWhenEmpty
import com.lobox.technical_assignment.util.toBool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path

//@Service
class TitleBasicsService(
    val titleBasicsRepository: TitleBasicsRepository,
    val genreRepository: GenreRepository,
    @Value("\${com.lobox.technical_assignment.csv_path}")
    val datasetPath: String,
    @Value("\${com.lobox.technical_assignment.batch_size}")
    val batchSize: Int,
    val jdbcTemplate: JdbcTemplate,
) : DataInitializeServiceInterface {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)

    companion object {
        private const val csvFileName = "title.basics.tsv"
        private const val titleInsertQuery = "INSERT INTO title_basic " +
                "(tconst, title_type, primary_title, original_title, is_adult, start_year, end_year, runtime_minutes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        private const val genreToTitleInsertQuery =
            "INSERT INTO title_basic_genres (genres_id, title_basics_entity_tconst)" +
                    "VALUES(?, ?)"
    }

    override fun initialize() {
        logger.info("initializing has been started TitleBasicsService")
        val start = System.currentTimeMillis()

        val genreNameToEntity = mutableMapOf<String, Long>()
        var titleToGenre = mutableListOf<Pair<String, Long>>()
        var titles = mutableListOf<List<String>>()
        try {
            Files.newBufferedReader(Path.of(datasetPath, csvFileName)).use { fileReader ->
                fileReader.readLine()
                var line = fileReader.readLine()
                while (line != null) {
                    val csvLine = line.split('\t')
                    val tconst = csvLine.first()
                    titleToGenre.addAll(csvLine
                        .last()
                        .split(",")
                        .mapNotNull { stringGenre ->
                            val foundGenre =
                                genreNameToEntity[stringGenre]
                                    ?: genreRepository.saveAndFlush(GenreEntity(name = stringGenre)).id
                                        ?.also { genreNameToEntity[stringGenre] = it } ?: return@mapNotNull null
                            tconst to foundGenre
                        }
                    )
                    titles.add(csvLine)
                    if (titles.size > batchSize) {
                        batchInsertTitles(titles)
                        titles = mutableListOf()
                        titleToGenre.chunked(batchSize).forEach { batchInsertGenres(it) }
                        titleToGenre = mutableListOf()
                    }
                    line = fileReader.readLine()
                }
                batchInsertTitles(titles)
                batchInsertGenres(titleToGenre)
            }
        } catch (e: Exception) {
            logger.error("TitleBasicsService finished in ${System.currentTimeMillis() - start}ms", e)
        }
        logger.info("TitleBasicsService finished in ${System.currentTimeMillis() - start}ms")
    }

    fun batchInsertTitles(csvLines: MutableList<List<String>>) {
        jdbcTemplate.batchUpdate(titleInsertQuery, csvLines, csvLines.size) { ps, seperatedColumns ->
            ps.setString(
                1,
                seperatedColumns[0].convertToNullWhenEmpty() ?: throw ImportException(
                    field = "tconst",
                    line = seperatedColumns
                )
            )
            ps.setString(
                2,
                seperatedColumns[1].convertToNullWhenEmpty() ?: throw ImportException(
                    field = "titleType",
                    line = seperatedColumns
                )
            )
            ps.setString(
                3,
                seperatedColumns[2].convertToNullWhenEmpty() ?: throw ImportException(
                    field = "primaryTitle",
                    line = seperatedColumns
                )
            )
            ps.setString(
                4,
                seperatedColumns[3].convertToNullWhenEmpty() ?: throw ImportException(
                    field = "originalTitle",
                    line = seperatedColumns
                )
            )
            ps.setBoolean(
                5,
                (seperatedColumns[4].convertToNullWhenEmpty() ?: throw ImportException(
                    field = "isAdult",
                    line = seperatedColumns
                )).toBool()
            )
            ps.setString(
                6,
                seperatedColumns[5].convertToNullWhenEmpty()
            )
            ps.setString(7, seperatedColumns[6].convertToNullWhenEmpty())
            ps.setInt(8, (seperatedColumns[7].convertToNullWhenEmpty())?.toInt() ?: 0)
        }
    }

    fun batchInsertGenres(csvLines: List<Pair<String, Long>>) {
        jdbcTemplate.batchUpdate(genreToTitleInsertQuery, csvLines, csvLines.size) { ps, (titleId, genreId) ->
            ps.setLong(1, genreId)
            ps.setString(2, titleId)
        }
    }

    fun getGenres() = genreRepository.findAll(Pageable.ofSize(100))

    fun getTitles() = titleBasicsRepository.findAll(Pageable.ofSize(10))
}