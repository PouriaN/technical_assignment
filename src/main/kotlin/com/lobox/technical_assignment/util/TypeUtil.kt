package com.lobox.technical_assignment.util

fun String.toBool() = when (this) {
    "1" -> true
    "true" -> true
    "True" -> true
    "TRUE" -> true
    else -> false
}