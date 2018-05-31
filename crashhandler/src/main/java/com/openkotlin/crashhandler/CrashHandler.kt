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
    private var isNeedSystemHandle: Boolean = true

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

    /**
     * install crash handler to application
     * @param application android application
     * @param isNeedSystemHandle if this parameter is true, the default crash handler will invoked finally
     */
    fun install(application: Application, isNeedSystemHandle: Boolean = true) {
        if (CrashHandleLooper.isLooperInstalled()) return
        application.registerActivityLifecycleCallbacks(this)
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        this.isNeedSystemHandle = isNeedSystemHandle
        CrashHandleLooper.install(this)
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        Thread().run {
            crashListener.handleCrashInAsync(e)
        }
        if (isNeedSystemHandle) {
            defaultExceptionHandler.uncaughtException(t, e)
        } else {
            activity?.finish()
        }
    }

    fun onDispatchEventCrash(t: Thread?, e: Throwable?) {
        Thread().run {
            crashListener.handleCrashInAsync(e)
        }
        activity?.runOnUiThread {
            crashListener.handleCrashInUiThread(e, activity!!)
            if (isNeedSystemHandle)
                defaultExceptionHandler.uncaughtException(t, e)
        }
    }

}