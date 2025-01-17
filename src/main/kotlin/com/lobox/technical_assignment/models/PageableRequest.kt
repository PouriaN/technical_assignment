package com.lobox.technical_assignment.models

data class PageableRequest(
    val pageSize: Int = 10,
    val pageNumber: Int = 0,
)