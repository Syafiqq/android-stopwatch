package com.github.syafiqq.dump.stopwatch

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import org.joda.time.Duration
import timber.log.Timber

/**
 * This android-stopwatch project created by :
 * Name         : syafiq
 * Date / Time  : 25 February 2018, 1:53 PM.
 * Email        : id.muhammad.syafiq@gmail.com
 * Github       : Syafiqq
 */
class App: Application()
{
    val stopwatchService: OStopwatchService = OStopwatchService()

    override fun onCreate()
    {
        Timber.d("onCreate")
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channelId = "android-stopwatch"
            val channelName = "stopwatch"
            getSystemService(NotificationManager::class.java).run {
                this?.createNotificationChannel(NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH))
            }
        }
        Timber.plant(Timber.DebugTree())
        App.instance = this
        this.bindStopwatchService()
    }

    private fun bindStopwatchService()
    {
        Timber.d("bindStopwatchService")

        super.bindService(Intent(this, StopwatchService::class.java), stopwatchServiceConnection, Context.BIND_AUTO_CREATE)
    }

    private val stopwatchServiceConnection = object: ServiceConnection
    {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?)
        {
            Timber.d("onServiceConnected [$name, $service]")

            stopwatchService.set((service as StopwatchService.StopwatchServiceBinder).service)
        }

        override fun onServiceDisconnected(name: ComponentName?)
        {
            Timber.d("onServiceConnected [$name]")

            stopwatchService.set(null)
        }
    }

    companion object
    {
        lateinit var instance: App
    }
}

fun Duration.toTimeElapsedString(): String
{
    return this.millis.toString()
}