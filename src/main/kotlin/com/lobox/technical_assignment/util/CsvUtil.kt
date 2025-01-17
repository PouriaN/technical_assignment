package com.lobox.technical_assignment.util

fun String.convertToNullWhenEmpty() = if(this == "\\N") null else this