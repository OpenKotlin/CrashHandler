package com.openkotlin.crashahandlerdemo

import android.app.Activity
import android.app.Application
import android.content.Intent
import com.openkotlin.crashhandler.CrashHandler
import com.openkotlin.crashhandler.CrashListener
import java.io.PrintWriter
import java.io.StringWriter

class DemoApplication : Application(), CrashListener {

    companion object {
        const val ERROR_INFO = "ERROR_INFO"
    }

    override fun onCreate() {
        super.onCreate()
        CrashHandler.of(this).install(this, false)
    }

    override fun handleCrashInUiThread(t: Throwable?, activity: Activity) {
        val errorInfo = getErrorInfo(t)
        val errorIntent = Intent(this, CrashActivity::class.java)
        errorIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        errorIntent.putExtra(ERROR_INFO, errorInfo)
        activity.finish()
        startActivity(errorIntent)
    }

    override fun handleCrashInAsync(t: Throwable?) {
        //Upload crash info
    }

    private fun getErrorInfo(t: Throwable?): String {
        if (t == null) return ""
        val sw = StringWriter()
        t.printStackTrace(PrintWriter(sw, true))
        return sw.buffer.toString()
    }
}