package com.openkotlin.crashhandler

import android.app.Activity

interface CrashListener {

    /**
     * Handle crash in Ui thread, but if crash happens in lifecycle method, this callback will not be invoked
     * @param t        error
     * @param activity current activity
     */
    fun handleCrashInUiThread(t: Throwable?, activity: Activity)

    /**
     * Handle crash in async thread
     */
    fun handleCrashInAsync(t: Throwable?)
}