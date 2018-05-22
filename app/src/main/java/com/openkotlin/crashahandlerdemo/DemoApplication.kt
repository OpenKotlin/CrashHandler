package com.openkotlin.crashahandlerdemo

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.view.View
import com.openkotlin.crashhandler.CrashHandler
import com.openkotlin.crashhandler.CrashListener
import java.io.PrintWriter
import java.io.StringWriter

class DemoApplication : Application(), CrashListener {

    companion object {
        const val ACTIVITY_VISIBILITY_OPTIONS = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        const val ERROR_INFO = "ERROR_INFO"
    }

    override fun onCreate() {
        super.onCreate()
        CrashHandler.of(this).install(this)
    }

    override fun onCrash(t: Throwable?, activity: Activity): Boolean {
        val errorInfo = getErrorInfo(t)
        val errorIntent = Intent(this, CrashActivity::class.java)
        errorIntent.putExtra(ERROR_INFO, errorInfo)
        activity.finish()
        startActivity(errorIntent)
        return false
    }

    private fun getErrorInfo(t: Throwable?): String {
        if (t == null) return ""
        val sw = StringWriter()
        t.printStackTrace(PrintWriter(sw, true))
        return sw.buffer.toString()
    }
}