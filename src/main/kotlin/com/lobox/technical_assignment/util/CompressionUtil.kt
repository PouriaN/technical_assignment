package com.lobox.technical_assignment.util

import org.xerial.snappy.Snappy
import java.nio.charset.StandardCharsets

fun String.snappyCompress() = Snappy.compress(this.toByteArray(StandardCharsets.UTF_8))
fun ByteArray.snappyDeCompress() = Snappy.uncompress(this).toString(StandardCharsets.UTF_8)
