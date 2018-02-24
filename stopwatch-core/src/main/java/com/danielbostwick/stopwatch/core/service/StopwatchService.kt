package com.danielbostwick.stopwatch.core.service

import com.danielbostwick.stopwatch.core.model.Stopwatch
import org.joda.time.Duration
import org.joda.time.LocalDateTime

interface StopwatchService
{
    fun create(): Stopwatch
    fun start(stopwatch: Stopwatch, startedAt: LocalDateTime): Stopwatch
    fun pause(stopwatch: Stopwatch, pausedAt: LocalDateTime): Stopwatch
    fun reset(stopwatch: Stopwatch): Stopwatch
    fun timeElapsed(stopwatch: Stopwatch, now: LocalDateTime): Duration
}
