package com.danielbostwick.stopwatch.core.command

import com.danielbostwick.stopwatch.core.model.Stopwatch
import com.danielbostwick.stopwatch.core.model.StopwatchState
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.notNullValue
import org.joda.time.DateTime
import org.joda.time.Duration
import org.junit.Test

class CommandsTest
{
    private val stopwatch = Stopwatch(StopwatchState.PAUSED, DateTime.now(), Duration.ZERO)

    @Test
    fun stopwatchStartCommandExists()
    {
        val startCmd = StopwatchStart(stopwatch, DateTime.now())
        assertThat(startCmd, notNullValue())
    }

    @Test
    fun stopwatchPauseCommandExists()
    {
        val pauseCmd = StopwatchPause(stopwatch, DateTime.now())
        assertThat(pauseCmd, notNullValue())
    }

    @Test
    fun stopwatchResetCommandExists()
    {
        val resetCmd = StopwatchReset(stopwatch)
        assertThat(resetCmd, notNullValue())
    }
}
