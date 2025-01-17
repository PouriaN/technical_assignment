package com.lobox.technical_assignment.services

import com.lobox.technical_assignment.util.convertToNullWhenEmpty
import com.lobox.technical_assignment.util.toBool
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.util.TreeMap

@Service
class NameBasicsService(
    @Value("\${com.lobox.technical_assignment.csv_path}")
    val datasetPath: String,
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    private val nameToAlive = TreeMap<String, Boolean>()

    companion object {
        private const val csvFileName = "name.basics.tsv"
    }

    @PostConstruct
    fun initialize() {
        val start = System.currentTimeMillis()
        try {
            logger.info("initializing has been started NameBasicsService")

            Files.newBufferedReader(Path.of(datasetPath, csvFileName)).use { fileReader ->
                fileReader.readLine()
                var line = fileReader.readLine()
                while (line != null) {
                    val csvLine = line.split('\t')
                    nameToAlive[csvLine[0]] = csvLine[3].convertToNullWhenEmpty().isNullOrEmpty()
                    line = fileReader.readLine()
                }
            }
            logger.info("NameBasicsService finished in ${System.currentTimeMillis() - start}ms")

        } catch (e: Exception) {
            logger.error("NameBasicsService finished in ${System.currentTimeMillis() - start}ms", e)
        }
    }

    fun getNames() = nameToAlive
}