package com.danielbostwick.stopwatch.core.event

import com.danielbostwick.stopwatch.core.model.Stopwatch
import com.danielbostwick.stopwatch.core.model.StopwatchState
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.notNullValue
import org.joda.time.Duration
import org.joda.time.LocalDateTime
import org.junit.Test

class EventsTest
{
    private val stopwatch = Stopwatch(StopwatchState.PAUSED, LocalDateTime.now(), Duration.ZERO)

    @Test
    fun stopwatchStartedEventExists()
    {
        val evt = StopwatchStarted(stopwatch)

        assertThat(evt, notNullValue())
        assertThat(evt.stopwatch, equalTo(stopwatch))
    }

    @Test
    fun stopwatchPausedEventExists()
    {
        val evt = StopwatchPaused(stopwatch)

        assertThat(evt, notNullValue())
        assertThat(evt.stopwatch, equalTo(stopwatch))
    }

    @Test
    fun stopwatchResetEventExists()
    {
        val evt = StopwatchWasReset(stopwatch)

        assertThat(evt, notNullValue())
        assertThat(evt.stopwatch, equalTo(stopwatch))
    }
}
