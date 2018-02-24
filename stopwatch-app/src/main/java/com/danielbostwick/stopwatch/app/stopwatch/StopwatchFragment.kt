package com.danielbostwick.stopwatch.app.stopwatch

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.danielbostwick.stopwatch.R
import com.danielbostwick.stopwatch.app.StopwatchApplication
import com.danielbostwick.stopwatch.core.manager.StopwatchManager
import com.danielbostwick.stopwatch.core.service.StopwatchService
import kotlinx.android.synthetic.main.fragment_stopwatch.*
import timber.log.Timber
import java.util.Timer
import java.util.TimerTask

class StopwatchFragment: Fragment(), StopwatchContract.View
{
    private val UPDATE_DELAY: Long = 100

    private val eventBus = StopwatchApplication.instance.eventBus
    private val stopwatchService = StopwatchApplication.instance.stopwatchService
    private lateinit var updateTimer: Timer

    private lateinit var presenter: StopwatchContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?): View
    {
        Timber.d("onCreateView [$inflater, $container, $state]")

        return inflater.inflate(R.layout.fragment_stopwatch, container, false)
    }

    override fun onViewCreated(view: View, state: Bundle?)
    {
        Timber.d("onViewCreated [$view, $state]")

        super.onViewCreated(view, state)
        setPresenter(StopwatchPresenter(this, eventBus, stopwatchService as StopwatchManager,
                stopwatchService as StopwatchService))

        fragment_stopwatch_start.setOnClickListener { presenter.onStopwatchStartClicked() }
        fragment_stopwatch_pause.setOnClickListener { presenter.onStopwatchPauseClicked() }
        fragment_stopwatch_reset.setOnClickListener { presenter.onStopwatchResetClicked() }
    }

    override fun onResume()
    {
        Timber.d("onResume")

        super.onResume()
        updateTimer = Timer()

        presenter.onResume()
        try
        {
            updateTimer.scheduleAtFixedRate(updateText, 0, UPDATE_DELAY)
        }
        catch (e: Exception)
        {
            updateText = createTimerTask()
            updateTimer.scheduleAtFixedRate(updateText, 0, UPDATE_DELAY)
            Timber.e(e)
        }
    }

    override fun onPause()
    {
        Timber.d("onResume")

        super.onPause()
        presenter.onPause()
        updateTimer.cancel()
    }

    override fun setPresenter(presenter: StopwatchContract.Presenter)
    {
        Timber.d("setPresenter [$presenter]")

        this.presenter = presenter
    }

    override fun showStartResetButtons()
    {
        Timber.d("showStartResetButtons")

        fragment_stopwatch_start_reset_container.visibility = View.VISIBLE
        fragment_stopwatch_pause_container.visibility = View.GONE
    }

    override fun showPauseButton()
    {
        Timber.d("showPauseButton")

        fragment_stopwatch_start_reset_container.visibility = View.GONE
        fragment_stopwatch_pause_container.visibility = View.VISIBLE
    }

    private var updateText = createTimerTask()

    private fun createTimerTask(): TimerTask
    {
        Timber.d("createTimerTask")

        return object: TimerTask()
        {
            override fun run()
            {
                activity?.runOnUiThread { fragment_stopwatch_time.text = presenter.getStopwatchTimeElapsed() }
            }
        }
    }
}
