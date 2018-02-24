package com.danielbostwick.stopwatch.app.stopwatch

import com.danielbostwick.stopwatch.core.event.StopwatchPaused
import com.danielbostwick.stopwatch.core.event.StopwatchStarted
import com.danielbostwick.stopwatch.core.event.StopwatchWasReset
import com.danielbostwick.stopwatch.core.manager.StopwatchManager
import com.danielbostwick.stopwatch.core.model.Stopwatch
import com.danielbostwick.stopwatch.core.model.StopwatchState
import com.danielbostwick.stopwatch.core.service.StopwatchService
import com.danielbostwick.stopwatch.ext.toTimeElapsedString
import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import org.joda.time.DateTime
import timber.log.Timber

class StopwatchPresenter(private val view: StopwatchContract.View,
                         private val eventBus: EventBus,
                         private val stopwatchManager: StopwatchManager,
                         private val stopwatchService: StopwatchService): StopwatchContract.Presenter
{
    var stopwatch: Stopwatch

    init
    {
        Timber.d("init")

        stopwatch = stopwatchManager.getStopwatch()
    }

    override fun onResume()
    {
        Timber.d("onResume")

        eventBus.register(this)
        stopwatch = stopwatchManager.getStopwatch()

        when (stopwatch.state)
        {
            StopwatchState.PAUSED  -> view.showStartResetButtons()
            StopwatchState.STARTED -> view.showPauseButton()
        }
    }

    override fun onPause()
    {
        Timber.d("onPause")

        eventBus.unregister(this)
    }

    override fun onStopwatchStartClicked()
    {
        Timber.d("onStopwatchStartClicked")

        stopwatchService.start(stopwatch, DateTime.now())
    }

    override fun onStopwatchPauseClicked()
    {
        Timber.d("onStopwatchPauseClicked")

        stopwatchService.pause(stopwatch, DateTime.now())
    }

    override fun onStopwatchResetClicked()
    {
        Timber.d("onStopwatchResetClicked")

        stopwatchService.reset(stopwatch)
    }

    override fun getStopwatchTimeElapsed(): String
    {
        //Timber.d("getStopwatchTimeElapsed")

        return stopwatchService.timeElapsed(stopwatch, DateTime.now()).toTimeElapsedString()
    }

    @Subscribe
    fun onStopwatchStarted(event: StopwatchStarted)
    {
        Timber.d("onStopwatchStarted")

        this.stopwatch = event.stopwatch
        view.showPauseButton()
    }

    @Subscribe
    fun onStopwatchPaused(event: StopwatchPaused)
    {
        Timber.d("onStopwatchPaused")

        this.stopwatch = event.stopwatch
        view.showStartResetButtons()
    }

    @Subscribe
    fun onStopwatchReset(event: StopwatchWasReset)
    {
        Timber.d("onStopwatchReset")

        this.stopwatch = event.stopwatch
        view.showStartResetButtons()
    }
}
