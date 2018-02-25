package com.danielbostwick.stopwatch.app.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import com.danielbostwick.stopwatch.R
import com.danielbostwick.stopwatch.app.StopwatchApplication
import com.danielbostwick.stopwatch.app.stopwatch.StopwatchActivity
import com.danielbostwick.stopwatch.core.event.StopwatchPaused
import com.danielbostwick.stopwatch.core.event.StopwatchStarted
import com.danielbostwick.stopwatch.core.event.StopwatchWasReset
import com.danielbostwick.stopwatch.core.manager.StopwatchManager
import com.danielbostwick.stopwatch.core.model.Stopwatch
import com.danielbostwick.stopwatch.core.service.DefaultStopwatchService
import com.danielbostwick.stopwatch.core.service.StopwatchService
import com.danielbostwick.stopwatch.ext.toTimeElapsedString
import org.joda.time.DateTime
import org.joda.time.Duration
import timber.log.Timber
import java.util.concurrent.atomic.AtomicReference


class StopwatchAndroidService: Service(), StopwatchService, StopwatchManager
{
    private val ONGOING_NOTIFICATION_ID = 100

    private val stopwatchRef: AtomicReference<Stopwatch> = AtomicReference()
    private val stopwatchService = DefaultStopwatchService()
    private var notification: Notification? = null
    private val binder = StopwatchServiceBinder()
    private val eventBus = StopwatchApplication.instance.eventBus

    override fun onCreate()
    {
        Timber.d("onCreate")

        super.onCreate()
        stopwatchRef.set(stopwatchService.create())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        Timber.d("onStartCommand [$intent, $flags, $startId]")

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder
    {
        Timber.d("onBind [$intent]")

        return binder
    }

    override fun getStopwatch(): Stopwatch
    {
        Timber.d("getStopwatch")

        return stopwatchRef.get()
    }

    override fun create(): Stopwatch
    {
        Timber.d("create")

        return stopwatchService.create()
    }

    override fun start(stopwatch: Stopwatch, startedAt: DateTime): Stopwatch
    {
        Timber.d("start [$stopwatch, $startedAt]")

        val newStopwatch = stopwatchService.start(stopwatch, startedAt)

        stopwatchRef.set(newStopwatch)
        eventBus.post(StopwatchStarted(newStopwatch))

        notification = notification ?: createNotification(newStopwatch)
        startForeground(ONGOING_NOTIFICATION_ID, notification)

        return newStopwatch
    }

    override fun pause(stopwatch: Stopwatch, pausedAt: DateTime): Stopwatch
    {
        Timber.d("pause [$stopwatch, $pausedAt]")

        val newStopwatch = stopwatchService.pause(stopwatch, pausedAt)

        stopwatchRef.set(newStopwatch)
        eventBus.post(StopwatchPaused(newStopwatch))

        return newStopwatch
    }

    override fun reset(stopwatch: Stopwatch): Stopwatch
    {
        Timber.d("reset [$stopwatch]")

        val newStopwatch = stopwatchService.reset(stopwatch)

        stopwatchRef.set(newStopwatch)
        eventBus.post(StopwatchWasReset(newStopwatch))

        stopForeground(true)

        return newStopwatch
    }

    override fun timeElapsed(stopwatch: Stopwatch, now: DateTime): Duration
    {
        return stopwatchService.timeElapsed(stopwatch, now)
    }


    private fun createNotification(stopwatch: Stopwatch): Notification
    {
        Timber.d("createNotification [$stopwatch]")

        val notificationIntent = Intent(this, StopwatchActivity::class.java)
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntent(notificationIntent)
        val pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        val timeElapsedStr = stopwatchService.timeElapsed(stopwatch, DateTime.now()).toTimeElapsedString()

        return NotificationCompat.Builder(this, "stopwatch")
                .setSmallIcon(R.drawable.abc_textfield_search_material)
                .setContentTitle(getText(R.string.notification_title))
                .setContentText(timeElapsedStr)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build()
    }

    /**
     * Class for clients to access.  Because we know this service always runs in the same process
     * as its clients, we don't need to deal with IPC.
     */
    inner class StopwatchServiceBinder: Binder()
    {
        internal val service: StopwatchAndroidService
            get() = this@StopwatchAndroidService
    }
}
