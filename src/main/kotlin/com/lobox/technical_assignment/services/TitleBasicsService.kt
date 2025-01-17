package com.lobox.technical_assignment.services

import com.lobox.technical_assignment.db.entities.TitleBasicsEntity
import com.lobox.technical_assignment.util.*
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.util.TreeMap

@Service
class TitleBasicsService(
    val titleCrewService: TitleCrewService,
    val nameBasicsService: NameBasicsService,
    @Value("\${com.lobox.technical_assignment.csv_path}")
    val datasetPath: String,
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    private val genreToTitles = mutableMapOf<String, MutableList<String>>()
    private val titleToContent = TreeMap<String, TitleBasicsEntity>()

    companion object {
        private const val csvFileName = "title.basics.tsv"
    }

    @PostConstruct
    fun initialize() {
        logger.info("initializing has been started TitleBasicsService")
        val start = System.currentTimeMillis()

        try {
            Files.newBufferedReader(Path.of(datasetPath, csvFileName)).use { fileReader ->
                fileReader.readLine()
                var line = fileReader.readLine()
                while (line != null) {
                    val csvLine = line.split('\t')
                    val tconst = csvLine.first()
                    val genresString = csvLine.last()

                    val genreList = genresString.convertToNullWhenEmpty()?.split(',')
                    genreList?.forEach { genre ->
                        val titles = genreToTitles[genre] ?: mutableListOf()
                        titles.add(tconst)
                        genreToTitles[genre] = titles
                    }

                    titleToContent[tconst] = TitleBasicsEntity.of(csvLine)

                    line = fileReader.readLine()
                }
            }
        } catch (e: Exception) {
            logger.error("TitleBasicsService finished in ${System.currentTimeMillis() - start}ms", e)
        }
        logger.info("TitleBasicsService finished in ${System.currentTimeMillis() - start}ms")
    }

    fun getTitlesIdWithSameAliveDirectorAndWriter(pageNumber: Int, pageSize: Int) =
        titleCrewService.getWriterAndDirectorToTitle()
            .filter { (writerAndDirector, _) -> nameBasicsService.getNames()[writerAndDirector] ?: false }
            .values
            .chunked(pageSize)[pageNumber]

    fun getTitlesIdWithSameAliveDirectorAndWriter() =
        titleCrewService.getWriterAndDirectorToTitle()
            .filter { (writerAndDirector, _) -> nameBasicsService.getNames()[writerAndDirector] ?: false }
            .mapNotNull { (_, title) -> title }

    fun getTitlesWithSameAliveDirectorAndWriter(pageNumber: Int, pageSize: Int) =
        titleCrewService.getWriterAndDirectorToTitle()
            .filter { (writerAndDirector, _) -> nameBasicsService.getNames()[writerAndDirector] ?: false }
            .keys
            .chunked(pageSize)[pageNumber]
            .mapNotNull { tconst -> titleToContent[tconst] }
}