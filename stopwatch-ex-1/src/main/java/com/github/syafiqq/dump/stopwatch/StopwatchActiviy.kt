package com.github.syafiqq.dump.stopwatch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_stopwatch_activiy.*
import org.joda.time.DateTime
import timber.log.Timber
import java.util.Timer
import java.util.TimerTask

class StopwatchActiviy: AppCompatActivity()
{
    private val UPDATE_DELAY: Long = 1000
    private var stopwatchService = App.instance.stopwatchService.service
    private lateinit var timer: Timer

    override fun onCreate(state: Bundle?)
    {
        Timber.d("onCreate [$state]")

        super.onCreate(state)
        setContentView(R.layout.activity_stopwatch_activiy)
        stopwatchService = App.instance.stopwatchService.service
        App.instance.stopwatchService.addObserver { o, arg -> if (o is OStopwatchService) this.stopwatchService = arg as StopwatchService }

        this.start1.setOnClickListener { this.stopwatchService?.run { this.start(this.getStopwatch(0), DateTime.now(), 0) } }
        this.start2.setOnClickListener { this.stopwatchService?.run { this.start(this.getStopwatch(1), DateTime.now(), 1) } }
        this.start3.setOnClickListener { this.stopwatchService?.run { this.start(this.getStopwatch(2), DateTime.now(), 2) } }
        this.start4.setOnClickListener { this.stopwatchService?.run { this.start(this.getStopwatch(3), DateTime.now(), 3) } }
        this.stop1.setOnClickListener { this.stopwatchService?.run { this@StopwatchActiviy.lap1.text = this.timeElapsed(this.getStopwatch(0), DateTime.now()).toTimeElapsedString() } }
        this.stop2.setOnClickListener { this.stopwatchService?.run { this@StopwatchActiviy.lap2.text = this.timeElapsed(this.getStopwatch(1), DateTime.now()).toTimeElapsedString() } }
        this.stop3.setOnClickListener { this.stopwatchService?.run { this@StopwatchActiviy.lap3.text = this.timeElapsed(this.getStopwatch(2), DateTime.now()).toTimeElapsedString() } }
        this.stop4.setOnClickListener { this.stopwatchService?.run { this@StopwatchActiviy.lap4.text = this.timeElapsed(this.getStopwatch(3), DateTime.now()).toTimeElapsedString() } }
    }

    override fun onResume()
    {
        Timber.d("onResume")

        super.onResume()
        timer = Timer()

        try
        {
            timer.scheduleAtFixedRate(updateText, 0, UPDATE_DELAY)
        }
        catch (e: Exception)
        {
            updateText = createTimerTask()
            timer.scheduleAtFixedRate(updateText, 0, UPDATE_DELAY)
            Timber.e(e)
        }
    }

    override fun onPause()
    {
        Timber.d("onResume")

        super.onPause()
        timer.cancel()
    }

    private var updateText = createTimerTask()

    private fun createTimerTask(): TimerTask
    {
        Timber.d("createTimerTask")

        return object: TimerTask()
        {
            override fun run()
            {
                this@StopwatchActiviy.runOnUiThread {
                    this@StopwatchActiviy.stopwatchService?.let {
                        this@StopwatchActiviy.counter1.text = it.timeElapsed(it.getStopwatch(0), DateTime.now()).toTimeElapsedString()
                        this@StopwatchActiviy.counter2.text = it.timeElapsed(it.getStopwatch(1), DateTime.now()).toTimeElapsedString()
                        this@StopwatchActiviy.counter3.text = it.timeElapsed(it.getStopwatch(2), DateTime.now()).toTimeElapsedString()
                        this@StopwatchActiviy.counter4.text = it.timeElapsed(it.getStopwatch(3), DateTime.now()).toTimeElapsedString()
                    }
                }
            }
        }
    }
}
