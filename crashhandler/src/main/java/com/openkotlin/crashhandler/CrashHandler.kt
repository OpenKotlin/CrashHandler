package com.openkotlin.crashhandler

import android.app.Activity
import android.app.Application
import android.os.Bundle

class CrashHandler private constructor(private val crashListener: CrashListener) : Application.ActivityLifecycleCallbacks, Thread.UncaughtExceptionHandler {
    companion object {
        fun of(crashListener: CrashListener) = CrashHandler(crashListener)
    }

    private var activity: Activity? = null
    private lateinit var defaultExceptionHandler: Thread.UncaughtExceptionHandler

    override fun onActivityPaused(activity: Activity?) {
    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity?) {
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        this.activity = activity
    }

    fun install(application: Application) {
        if (CrashHandleLooper.isLooperInstalled()) return
        application.registerActivityLifecycleCallbacks(this)
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        CrashHandleLooper.setCrashHandler(this)
        CrashHandleLooper.install()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        activity?.runOnUiThread {
            val needSystemHandle = crashListener.onCrash(e, activity!!)
            if (needSystemHandle) defaultExceptionHandler.uncaughtException(t, e)
        }
    }

}