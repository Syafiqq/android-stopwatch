package com.github.syafiqq.dump.stopwatch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import timber.log.Timber

class StopwatchActiviy: AppCompatActivity()
{

    override fun onCreate(state: Bundle?)
    {
        Timber.d("onCreate [$state]")

        super.onCreate(state)
        setContentView(R.layout.activity_stopwatch_activiy)
    }
}
