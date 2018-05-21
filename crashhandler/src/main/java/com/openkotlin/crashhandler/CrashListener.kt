package com.openkotlin.crashhandler

import android.app.Activity

interface CrashListener {

    /**
     * @param t        error
     * @param activity current activity
     * @return if return true, android system will handle the error finally
     */
    fun onCrash(t: Throwable?, activity: Activity): Boolean
}