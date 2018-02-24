package com.danielbostwick.stopwatch.core.model

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.notNullValue
import org.joda.time.Duration
import org.joda.time.LocalDateTime
import org.junit.Test

class ModelsTest
{
    @Test
    fun stopwatchCanBeInstantiated()
    {
        val s = Stopwatch(StopwatchState.PAUSED, LocalDateTime.now(), Duration.ZERO)

        assertThat(s, notNullValue())
    }
}
