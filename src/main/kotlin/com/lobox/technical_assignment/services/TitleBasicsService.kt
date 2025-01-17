package com.lobox.technical_assignment.services

import com.lobox.technical_assignment.models.Page
import com.lobox.technical_assignment.models.PageableRequest
import com.lobox.technical_assignment.models.TitleBasicsModel
import com.lobox.technical_assignment.util.*
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.TreeMap

@Service
class TitleBasicsService(
    val titleCrewService: TitleCrewService,
    val titlePrincipalService: TitlePrincipalService,
    val titleRatingService: TitleRatingService,
    val nameBasicsService: NameBasicsService,
    @Value("\${com.lobox.technical_assignment.csv_path}")
    val datasetPath: String,
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    private val genreToTitles = mutableMapOf<String, MutableList<String>>()
    private val titleToContent = TreeMap<String, TitleBasicsModel>()

    companion object {
        private const val csvFileName = "title.basics.tsv"
    }

    @PostConstruct
    fun initialize() {
        logger.info("initializing has been started TitleBasicsService")
        val start = System.currentTimeMillis()

        try {
            readCsv(directory = datasetPath, fileName = csvFileName) { columns ->
                val tconst = columns.first()
                val genresString = columns.last()

                val genreList = genresString.convertToNullWhenEmpty()?.split(',')
                genreList?.forEach { genre ->
                    val titles = genreToTitles[genre] ?: mutableListOf()
                    titles.add(tconst)
                    genreToTitles[genre] = titles
                }

                titleToContent[tconst] = TitleBasicsModel.of(columns)
            }
        } catch (e: Exception) {
            logger.error("TitleBasicsService finished in ${System.currentTimeMillis() - start}ms", e)
        }
        logger.info("TitleBasicsService finished in ${System.currentTimeMillis() - start}ms")
    }

    fun getTitlesWithSameAliveDirectorAndWriter(pageableRequest: PageableRequest): Page<TitleBasicsModel> {
        val titleChunks = titleCrewService.getWriterAndDirectorToTitle()
            .filter { (writerAndDirector, _) -> nameBasicsService.isPersonAlive(writerAndDirector) ?: false }
            .values
            .chunked(pageableRequest.pageSize)
        val titles = titleChunks[pageableRequest.pageNumber]
            .mapNotNull { tconst -> titleToContent[tconst] }
        return Page(items = titles, currentPage = pageableRequest.pageNumber, totalPage = titleChunks.size)
    }

    fun getTitlesPlayedByActor(actors: List<String>): List<TitleBasicsModel> {
        return actors
            .mapNotNull { actor ->
                titlePrincipalService.getPrincipalTitles(
                    actor,
                    TitlePrincipalService.Companion.Job.ACTOR
                )
            }
            .flatten()
            .groupBy { it }
            .mapNotNull { (tconst,  repeatedTitles) ->
                if (repeatedTitles.size > 1) titleToContent[tconst] else return@mapNotNull null
            }
    }

    fun bestGenreTitles(genre: String) = genreToTitles[genre]
        ?.sortedByDescending { tconst -> titleRatingService.getTitleRate(tconst) }
        ?.mapNotNull { tconst -> titleToContent[tconst] }
        ?.groupBy { it.startYear }
        ?.mapValues { (_, titles) -> if (titles.size > 10) titles.subList(0, 9) else titles }
}