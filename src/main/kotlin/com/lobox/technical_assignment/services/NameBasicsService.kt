package com.lobox.technical_assignment.services

import com.lobox.technical_assignment.util.convertToNullWhenEmpty
import com.lobox.technical_assignment.util.readCsv
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.TreeMap

@Service
class NameBasicsService(
    @Value("\${com.lobox.technical_assignment.csv_path}")
    val datasetPath: String,
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    private val isPersonAlive = TreeMap<String, Boolean>()

    companion object {
        private const val csvFileName = "name.basics.tsv"
    }

    @PostConstruct
    fun initialize() {
        val start = System.currentTimeMillis()
        try {
            logger.info("initializing has been started NameBasicsService")

            readCsv(directory = datasetPath, fileName = csvFileName) { columns ->
                isPersonAlive[columns[0]] = columns[3].convertToNullWhenEmpty().isNullOrEmpty()
            }
            logger.info("NameBasicsService finished in ${System.currentTimeMillis() - start}ms")
        } catch (e: Exception) {
            logger.error("NameBasicsService finished in ${System.currentTimeMillis() - start}ms", e)
        }
    }

    fun isPersonAlive(tconst: String) = isPersonAlive[tconst]
}