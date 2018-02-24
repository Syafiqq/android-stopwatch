package com.danielbostwick.stopwatch.core.model

import org.joda.time.Duration
import org.joda.time.LocalDateTime

data class Stopwatch(
        val state: StopwatchState,
        val startedAt: LocalDateTime,
        val offset: Duration)

enum class StopwatchState
{ PAUSED, STARTED }
