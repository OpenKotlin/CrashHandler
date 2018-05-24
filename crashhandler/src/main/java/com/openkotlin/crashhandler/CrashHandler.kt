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
    private var lifecycleMethodSignArray = arrayListOf(
            "onCreate",
            "onStart",
            "onRestart",
            "onResume",
            "onPause",
            "onStop",
            "onDestroy"
    )

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
        CrashHandleLooper.setCrashHandler(this)
        CrashHandleLooper.install()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        Thread().run {
            crashListener.handleCrashInAsync(e)
        }
        if (isLifecycleCrash(e)) {
            if (isNeedSystemHandle) {
                defaultExceptionHandler.uncaughtException(t, e)
            } else {
                activity?.finish()
            }
        } else {
            activity?.runOnUiThread {
                crashListener.handleCrashInUiThread(e, activity!!)
                if (isNeedSystemHandle)
                    defaultExceptionHandler.uncaughtException(t, e)
            }
        }
    }

    private fun isLifecycleCrash(e: Throwable?): Boolean {
        if (e == null) return false
        for (element in e.stackTrace) {
            if (lifecycleMethodSignArray.contains(element.methodName)) return true
        }
        if (e.cause != null) return isLifecycleCrash(e.cause)
        return false
    }

}