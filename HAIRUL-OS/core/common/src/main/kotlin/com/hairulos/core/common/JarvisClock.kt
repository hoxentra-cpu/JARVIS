package com.hairulos.core.common

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/** Time formatting and a simple ticking flow shared by the apps. */
object JarvisClock {
    private fun format(millis: Long, pattern: String): String =
        DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
            .format(Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()))

    fun time(millis: Long): String = format(millis, "HH:mm:ss")
    fun shortTime(millis: Long): String = format(millis, "HH:mm")
    fun date(millis: Long): String = format(millis, "EEE, d MMM yyyy")

    /** Emits the current epoch millis immediately and then every [periodMs]. */
    fun ticker(periodMs: Long = 1_000L): Flow<Long> = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(periodMs)
        }
    }
}
