package com.lobox.technical_assignment.services

import com.lobox.technical_assignment.exceptions.ImportException
import com.lobox.technical_assignment.util.convertToNullWhenEmpty
import com.lobox.technical_assignment.util.readCsv
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class TitlePrincipalService(
    @Value("\${com.lobox.technical_assignment.csv_path}")
    val datasetPath: String,
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    private val principalToJobCategoryToTitles = TreeMap<String, MutableMap<String, MutableList<String>>>()

    companion object {
        private const val csvFileName = "title.principals.tsv"
        private val acceptableJobCategories = listOf(Job.ACTOR.title)

        enum class Job(val title: String) {
            ACTOR("actor")
        }
    }

    @PostConstruct
    fun initialize() {
        logger.info("initializing has been started TitlePrincipalService")
        val start = System.currentTimeMillis()

        try {
            readCsv(directory = datasetPath, fileName = csvFileName, skipLines = 1, delimiter = '\t') { columns ->
                val tconst = columns[0].convertToNullWhenEmpty() ?: throw ImportException("tconst")
                val nconst = columns[2].convertToNullWhenEmpty() ?: throw ImportException("nconst")
                val jobCategory = columns[3].convertToNullWhenEmpty() ?: throw ImportException("jobCategory")

                if (acceptableJobCategories.contains(jobCategory)) {
                    val jobToTitles = principalToJobCategoryToTitles[nconst] ?: mutableMapOf()
                    val titles = jobToTitles[jobCategory] ?: mutableListOf(tconst)
                    jobToTitles[jobCategory] = titles
                    principalToJobCategoryToTitles[nconst] = jobToTitles
                }
            }
        } catch (e: Exception) {
            logger.error("TitlePrincipalService finished in ${System.currentTimeMillis() - start}ms", e)
        }

        logger.info("TitlePrincipalService finished in ${System.currentTimeMillis() - start}ms")
    }

    fun getPrincipalTitles(tconst: String, job: Job) = principalToJobCategoryToTitles[tconst]?.get(job.title)
}