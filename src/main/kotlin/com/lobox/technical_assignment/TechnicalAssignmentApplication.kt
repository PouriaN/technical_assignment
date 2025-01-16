package com.lobox.technical_assignment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication
@EnableAsync
class TechnicalAssignmentApplication

fun main(args: Array<String>) {
    runApplication<TechnicalAssignmentApplication>(*args)
}
