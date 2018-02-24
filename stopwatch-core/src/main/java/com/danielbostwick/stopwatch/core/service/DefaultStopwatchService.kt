package com.danielbostwick.stopwatch.core.service

import com.danielbostwick.stopwatch.core.model.Stopwatch
import com.danielbostwick.stopwatch.core.model.StopwatchState.PAUSED
import com.danielbostwick.stopwatch.core.model.StopwatchState.STARTED
import org.joda.time.Duration
import org.joda.time.Interval
import org.joda.time.LocalDateTime


class DefaultStopwatchService: StopwatchService
{
    override fun create() = Stopwatch(PAUSED, LocalDateTime.now(), Duration.ZERO)

    override fun start(stopwatch: Stopwatch, startedAt: LocalDateTime) = when (stopwatch.state)
    {
        PAUSED  -> Stopwatch(STARTED, LocalDateTime.now(), stopwatch.offset)
        STARTED -> stopwatch
    }

    override fun pause(stopwatch: Stopwatch, pausedAt: LocalDateTime) = when (stopwatch.state)
    {
        PAUSED  -> stopwatch
        STARTED -> Stopwatch(PAUSED, LocalDateTime.now(),
                newOffset(stopwatch.offset, stopwatch.startedAt, pausedAt))
    }

    override fun reset(stopwatch: Stopwatch) = create()

    override fun timeElapsed(stopwatch: Stopwatch, now: LocalDateTime): Duration = when (stopwatch.state)
    {
        PAUSED  -> stopwatch.offset
        STARTED -> stopwatch.offset.plus(Interval(stopwatch.startedAt.chronology, now.chronology).toDuration())
    }

    private fun newOffset(existingOffset: Duration, startedAt: LocalDateTime, pausedAt: LocalDateTime) =
            existingOffset.plus(Interval(startedAt.chronology, pausedAt.chronology).toDuration())
}
