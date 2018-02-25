package com.danielbostwick.stopwatch.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import com.danielbostwick.stopwatch.app.service.StopwatchAndroidService
import com.google.common.eventbus.EventBus
import timber.log.Timber


class StopwatchApplication: Application()
{
    val eventBus = EventBus("stopwatch")
    var stopwatchService: StopwatchAndroidService? = null

    override fun onCreate()
    {
        Timber.d("onCreate")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channelId = "android-stopwatch"
            val channelName = "stopwatch"
            getSystemService(NotificationManager::class.java).run {
                this?.createNotificationChannel(NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH))
            }
        }
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        StopwatchApplication.instance = this
        bindStopwatchService()
    }

    private fun bindStopwatchService()
    {
        Timber.d("bindStopwatchService")

        bindService(
                Intent(this, StopwatchAndroidService::class.java),
                stopwatchServiceConnection,
                Context.BIND_AUTO_CREATE)
    }

    private val stopwatchServiceConnection = object: ServiceConnection
    {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?)
        {
            Timber.d("onServiceConnected [$name, $service]")

            stopwatchService = (service as StopwatchAndroidService.StopwatchServiceBinder).service
            eventBus.post(StopwatchServiceBound())
        }

        override fun onServiceDisconnected(name: ComponentName?)
        {
            Timber.d("onServiceConnected [$name]")

            stopwatchService = null
        }
    }

    companion object
    {
        lateinit var instance: StopwatchApplication
    }
}
