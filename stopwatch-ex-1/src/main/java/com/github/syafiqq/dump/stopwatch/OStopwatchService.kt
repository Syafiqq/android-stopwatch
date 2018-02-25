package com.github.syafiqq.dump.stopwatch

import java.util.Observable

/**
 * This android-stopwatch project created by :
 * Name         : syafiq
 * Date / Time  : 25 February 2018, 3:15 PM.
 * Email        : id.muhammad.syafiq@gmail.com
 * Github       : Syafiqq
 */

class OStopwatchService(var service: StopwatchService? = null): Observable()
{
    fun set(service: StopwatchService?)
    {
        this.service = service
        setChanged()
        notifyObservers(this.service)
    }
}