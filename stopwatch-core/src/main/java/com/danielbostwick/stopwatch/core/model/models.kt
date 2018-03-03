package com.danielbostwick.stopwatch.core.model

import org.joda.time.DateTime
import org.joda.time.Duration
import java.io.Serializable

data class Stopwatch(var state: StopwatchState = StopwatchState.PAUSED, var startedAt: DateTime = DateTime.now(), var offset: Duration = Duration.ZERO):
        Serializable

enum class StopwatchState
{ PAUSED, STARTED }
