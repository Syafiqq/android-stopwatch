package com.danielbostwick.stopwatch.core.model

import org.joda.time.DateTime
import org.joda.time.Duration

data class Stopwatch(val state: StopwatchState = StopwatchState.PAUSED, val startedAt: DateTime = DateTime.now(), val offset: Duration = Duration.ZERO)

enum class StopwatchState
{ PAUSED, STARTED }
