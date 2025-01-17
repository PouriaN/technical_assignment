package com.lobox.technical_assignment.services

import com.lobox.technical_assignment.exceptions.ImportException
import com.lobox.technical_assignment.util.convertToNullWhenEmpty
import com.lobox.technical_assignment.util.readCsv
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.apache.commons.math3.distribution.NormalDistribution
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

@Service
class TitleRatingService(
    @Value("\${com.lobox.technical_assignment.csv_path}")
    val datasetPath: String,
) {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    private val titleToWilsonLowerBound = mutableMapOf<String, Double>()

    companion object {
        private const val csvFileName = "title.ratings.tsv"
    }

    @PostConstruct
    fun initialize() {
        logger.info("initializing has been started TitleRatingService")
        val start = System.currentTimeMillis()

        try {
            readCsv(directory = datasetPath, fileName = csvFileName) { columns ->
                val tconst = columns[0].convertToNullWhenEmpty() ?: throw ImportException("tconst")
                val averageRating =
                    columns[1].convertToNullWhenEmpty()?.toDouble() ?: throw ImportException("averageRating")
                val numVotes = columns[2].convertToNullWhenEmpty()?.toInt() ?: throw ImportException("numVotes")
                titleToWilsonLowerBound[tconst] = wilsonLowerBound(rate = averageRating, votesNumber = numVotes)
            }
        } catch (e: Exception) {
            logger.error("TitleRatingService finished in ${System.currentTimeMillis() - start}ms", e)
        }

        logger.info("TitleRatingService finished in ${System.currentTimeMillis() - start}ms")
    }

    fun wilsonLowerBound(rate: Double, votesNumber: Int, confidence: Double = 0.95): Double {
        if (votesNumber == 0) return 0.0
        val normalizedRate = (rate - 1) / (10 - 1)
        val z = NormalDistribution().inverseCumulativeProbability(1 - (1 - confidence) / 2)
        return (normalizedRate + z.pow(2) / (2 * votesNumber) - z * sqrt((normalizedRate * (1 - normalizedRate) + z.pow(2) / (4 * votesNumber)) / votesNumber)) /
                (1 + z.pow(2) / votesNumber)
    }

    fun getTitleRate(tconst: String) = titleToWilsonLowerBound[tconst]
}