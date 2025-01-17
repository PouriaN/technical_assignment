package com.lobox.technical_assignment.models

data class Page<T>(
    val items: List<T>,
    val currentPage: Int,
    val totalPage: Int,
)