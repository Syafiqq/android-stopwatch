package com.danielbostwick.stopwatch.app.stopwatch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.danielbostwick.stopwatch.R
import com.danielbostwick.stopwatch.app.StopwatchApplication
import com.danielbostwick.stopwatch.app.StopwatchServiceBound
import com.google.common.eventbus.Subscribe
import timber.log.Timber

class StopwatchActivity: AppCompatActivity()
{
    private val eventBus = StopwatchApplication.instance.eventBus
    private val stopwatchService = StopwatchApplication.instance.stopwatchService

    override fun onCreate(state: Bundle?)
    {
        Timber.d("onCreate [$state]")

        super.onCreate(state)
        setContentView(R.layout.activity_stopwatch)
    }

    override fun onResume()
    {
        Timber.d("onResume")

        super.onResume()
        eventBus.register(this)
        if (stopwatchService != null) showStopwatch()
    }

    override fun onPause()
    {
        Timber.d("onPause")

        eventBus.unregister(this)
        super.onPause()
    }

    @Subscribe
    fun onStopwatchServiceBound(event: StopwatchServiceBound)
    {
        Timber.d("onStopwatchServiceBound [$event]")

        showStopwatch()
    }

    private fun showStopwatch()
    {
        Timber.d("showStopwatch")

        supportFragmentManager.beginTransaction()
                .add(R.id.activity_stopwatch_container, StopwatchFragment())
                .commit()
    }
}
