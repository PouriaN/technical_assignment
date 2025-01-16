package com.lobox.technical_assignment.util

import com.lobox.technical_assignment.exceptions.ImportException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.Async

open class BatchIoUtil {
    companion object {
        @Async
        open fun batchInsert(
            jdbcTemplate: JdbcTemplate,
            query: String,
            csvLines: MutableList<List<String>>
        ) {
            jdbcTemplate.batchUpdate(query, csvLines, csvLines.size) { ps, seperatedColumns ->
                ps.setString(
                    1,
                    seperatedColumns[0].convertToNullWhenEmpty() ?: throw ImportException(
                        field = "nconst",
                        line = seperatedColumns
                    )
                )
                ps.setString(2, seperatedColumns[1].convertToNullWhenEmpty())
                ps.setString(3, seperatedColumns[2].convertToNullWhenEmpty())
                ps.setString(4, seperatedColumns[3].convertToNullWhenEmpty())
            }
        }
    }

}