package com.lobox.technical_assignment.configurations

import com.lobox.technical_assignment.services.DataInitializeServiceInterface
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DatasetInitializer(val dataInitializers: List<DataInitializeServiceInterface>): CommandLineRunner {
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)
    override fun run(vararg args: String?) {
        dataInitializers.forEach {
            try {
                it.initialize()
            } catch (e: Exception) {
                logger.error(e.message)
            }
        }
    }
}