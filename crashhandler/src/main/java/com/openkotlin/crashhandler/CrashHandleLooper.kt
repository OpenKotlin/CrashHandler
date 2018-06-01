package com.openkotlin.crashhandler

import android.os.*
import android.util.Log
import java.lang.reflect.Field
import java.lang.reflect.Method

internal class CrashHandleLooper(private var crashHandler: CrashHandler) : Runnable {

    companion object {
        private val TAG = CrashHandleLooper::class.simpleName
        private val CRASH_THREAD_LOCAL: ThreadLocal<CrashHandleLooper> = ThreadLocal()
        private val EXIT: Any = Any()
        private val mainHandler: Handler = Handler(Looper.getMainLooper())

        fun isLooperInstalled() = CRASH_THREAD_LOCAL.get() != null

        fun install(crashHandler: CrashHandler) {
            mainHandler.removeMessages(0, EXIT)
            mainHandler.post(CrashHandleLooper(crashHandler))
        }
    }

    override fun run() {
        if (isLooperInstalled()) return
        val next: Method
        val target: Field
        try {
            val method: Method = MessageQueue::class.java.getDeclaredMethod("next")
            method.isAccessible = true
            next = method
            val field: Field = Message::class.java.getDeclaredField("target")
            field.isAccessible = true
            target = field
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
            return
        }
        CRASH_THREAD_LOCAL.set(this)
        val messageQueue: MessageQueue = Looper.myQueue()
        Binder.clearCallingIdentity()

        while (true) {
            try {
                val message: Message? = next.invoke(messageQueue) as Message
                if (message == null || message.obj == EXIT) break
                val handler: Handler = target.get(message) as Handler
                handler.dispatchMessage(message)
                Binder.clearCallingIdentity()
            } catch (e: Exception) {
                crashHandler.onDispatchEventCrash(Thread.currentThread(), e)

                Handler().post(this)
            }
        }
        CRASH_THREAD_LOCAL.set(null)
    }

}