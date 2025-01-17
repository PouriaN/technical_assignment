package com.lobox.technical_assignment.services

import com.lobox.technical_assignment.exceptions.ImportException
import com.lobox.technical_assignment.util.convertToNullWhenEmpty
import com.lobox.technical_assignment.util.readCsv
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.util.TreeMap

@Service
class TitleCrewService(
    @Value("\${com.lobox.technical_assignment.csv_path}")
    val datasetPath: String,
) {
    companion object {
        private const val csvFileName = "title.crew.tsv"
    }
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    private val writerAndDirectorToTitle = TreeMap<String, String>()

    @PostConstruct
    fun initialize() {
        logger.info("initializing has been started TitleCrewService")
        val start = System.currentTimeMillis()
        try {
            readCsv(directory = datasetPath, fileName = csvFileName) { columns ->
                    val tconst = columns[0].convertToNullWhenEmpty() ?: throw throw ImportException(field = "tconst")
                    val directors = columns[1].convertToNullWhenEmpty()?.split(",")
                    val writers = columns[2].convertToNullWhenEmpty()?.split(",")

                    val directorAndWriter = findTheSameName(directors, writers)
                    if (!directorAndWriter.isNullOrEmpty()) writerAndDirectorToTitle[directorAndWriter] = tconst
            }
        } catch (e: Exception) {
            logger.error("TitleCrewService finished in ${System.currentTimeMillis() - start}ms", e)
        }
        logger.info("TitleCrewService finished in ${System.currentTimeMillis() - start}ms")
    }

    fun getWriterAndDirectorToTitle() = writerAndDirectorToTitle

    fun findTheSameName(directors: List<String>?, writers: List<String>?) =
        directors?.firstOrNull { director -> writers?.contains(director) ?: false }
}