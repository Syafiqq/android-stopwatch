package com.danielbostwick.stopwatch.core.model

import org.joda.time.DateTime
import org.joda.time.Duration

data class Stopwatch(var state: StopwatchState = StopwatchState.PAUSED, var startedAt: DateTime = DateTime.now(), var offset: Duration = Duration.ZERO)

enum class StopwatchState
{ PAUSED, STARTED }
