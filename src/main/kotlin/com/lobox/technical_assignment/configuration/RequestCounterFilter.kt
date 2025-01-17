package com.lobox.technical_assignment.configuration

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@Component
class RequestCounterFilter: Filter {
    private val requestCounter = AtomicInteger(0)
    private val logger = LoggerFactory.getLogger(javaClass.simpleName)

    override fun doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {
        logger.info("${requestCounter.incrementAndGet()}'th request has been received")
        filterChain.doFilter(request, response)
    }
}