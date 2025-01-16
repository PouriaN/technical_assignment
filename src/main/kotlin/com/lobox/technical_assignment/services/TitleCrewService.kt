package com.lobox.technical_assignment.services

import com.lobox.technical_assignment.db.entities.TitleDirectorEntity
import com.lobox.technical_assignment.db.entities.TitleWriterEntity
import com.lobox.technical_assignment.db.repositories.TitleDirectorRepository
import com.lobox.technical_assignment.db.repositories.TitleWriterRepository
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
class TitleCrewService(
    @Value("\${com.lobox.technical_assignment.csv_path}")
    val datasetPath: String,
    @Value("\${com.lobox.technical_assignment.batch_size}")
    val batchSize: Int,
    val jdbcTemplate: JdbcTemplate,
    val writerRepository: TitleWriterRepository,
    val directorRepository: TitleDirectorRepository,
) : DataInitializeServiceInterface {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    companion object {
        private const val csvFileName = "title.crew.tsv"
        private const val insertDirectoryQuery = "INSERT INTO title_director (tconst, director) VALUES (?, ?)"
        private const val insertWriterQuery = "INSERT INTO title_writer (tconst, writer) VALUES (?, ?)"
    }

    override fun initialize() {
        logger.info("initializing has been started TitleCrewService")
        val start = System.currentTimeMillis()
        var titleDirectors = mutableListOf<TitleDirectorEntity>()
        var titleWriters = mutableListOf<TitleWriterEntity>()
        try {
            Files.newBufferedReader(Path.of(datasetPath, csvFileName)).use { fileReader ->
                fileReader.readLine()
                var line = fileReader.readLine()
                while (line != null) {
                    val csvLine = line.split('\t')
                    val tconst = csvLine[0].convertToNullWhenEmpty() ?: throw throw ImportException(
                        field = "tconst",
                        line = csvLine
                    )
                    csvLine[1].convertToNullWhenEmpty()?.split(",")?.forEach { director ->
                        titleDirectors.add(
                            TitleDirectorEntity(
                                tconst = tconst,
                                director = director
                            )
                        )
                    }
                    csvLine[2].convertToNullWhenEmpty()?.split(",")?.forEach { writer ->
                        titleWriters.add(
                            TitleWriterEntity(
                                tconst = tconst,
                                writer = writer
                            )
                        )
                    }
                    if (titleDirectors.size > batchSize) {
                        insertDirectors(titleDirectors)
                        titleDirectors = mutableListOf()
                    }
                    if (titleWriters.size > batchSize) {
                        insertWriters(titleWriters)
                        titleWriters = mutableListOf()
                    }
                    line = fileReader.readLine()
                }
                insertDirectors(titleDirectors)
                insertWriters(titleWriters)

            }
        } catch (e: Exception) {
            logger.error("TitleCrewService finished in ${System.currentTimeMillis() - start}ms", e)
        }
        logger.info("TitleCrewService finished in ${System.currentTimeMillis() - start}ms")
    }

    fun insertDirectors(
        titles: MutableList<TitleDirectorEntity>
    ): Array<out IntArray> {
        val batchUpdate = jdbcTemplate.batchUpdate(insertDirectoryQuery, titles, titles.size) { ps, title ->
            ps.setString(1, title.tconst)
            ps.setString(2, title.director)
        }
        return batchUpdate
    }


    fun insertWriters(
        titles: MutableList<TitleWriterEntity>
    ): Array<out IntArray> {
        val batchUpdate = jdbcTemplate.batchUpdate(insertWriterQuery, titles, titles.size) { ps, title ->
            ps.setString(1, title.tconst)
            ps.setString(2, title.writer)
        }
        return batchUpdate
    }

    fun getTopWriter() = writerRepository.findAll(Pageable.ofSize(10))
    fun getTopDirector() = directorRepository.findAll(Pageable.ofSize(10))
}